package net.modificationstation.stationapi.api.resource.language;

import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import lombok.val;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.event.resource.language.TranslationInvalidationEvent;
import net.modificationstation.stationapi.api.client.resource.ReloadableAssetsManager;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.IdentifiableResourceReloadListener;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.SinglePreparationResourceReloader;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.DummyProfiler;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import net.modificationstation.stationapi.mixin.lang.TranslationStorageAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.quote;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Environment(EnvType.CLIENT)
@Entrypoint(eventBus = @EventBusPolicy(registerStatic = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class LanguageManager extends SinglePreparationResourceReloader<Map<Object, Object>> implements IdentifiableResourceReloadListener {
    public static final Identifier LANGUAGES = MODID.id("languages");
    @Entrypoint.Instance
    private static final LanguageManager INSTANCE = Null.get();
    @NotNull
    private static Predicate<String> pathPredicate = buildPathPredicate("en_US");
    private static final Object2ReferenceMap<String, ModID> LANG_PATHS = Util.make(new Object2ReferenceOpenHashMap<>(), paths -> {
        paths.put("/lang", ModID.MINECRAFT);
        paths.put(MODID + "/lang", null);
    });

    private static Predicate<String> buildPathPredicate(String langDef) {
        return Pattern
                .compile(".+(?:" + quote(langDef) + "|" + quote("stats_" + langDef.split("_")[1]) + ")" + quote(".lang"))
                .asPredicate();
    }

    public static void changeLanguage(String langDef) {
        pathPredicate = buildPathPredicate(langDef);
        INSTANCE.apply(
                INSTANCE.prepare(
                        ReloadableAssetsManager.INSTANCE,
                        DummyProfiler.INSTANCE
                ),
                ReloadableAssetsManager.INSTANCE,
                DummyProfiler.INSTANCE
        );
    }

    public static void addPath(String path) {
        LANG_PATHS.put(path, ModID.MINECRAFT);
    }

    public static void addPath(String path, ModID namespace) {
        LANG_PATHS.put(path, namespace);
    }

    @EventListener
    private void registerToManager(AssetsResourceReloaderRegisterEvent event) {
        event.resourceManager.registerReloader(this);
    }

    @Override
    protected Map<Object, Object> prepare(ResourceManager manager, Profiler profiler) {
        profiler.startTick();

        profiler.push("load");
        val result = LANG_PATHS.entrySet()
                .stream()
                .flatMap(pathNsEntry -> manager.findAllResources(pathNsEntry.getKey(), identifier -> pathPredicate.test(identifier.id)).entrySet()
                        .stream()
                        .flatMap(resourceEntry -> resourceEntry.getValue()
                                .stream()
                                .flatMap(resource -> {
                                    val properties = new Properties();
                                    try (InputStream stream = resource.getInputStream()) {
                                        properties.load(stream);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    val langNs = pathNsEntry.getValue() == null ? resourceEntry.getKey().modID : pathNsEntry.getValue();
                                    if (langNs == ModID.MINECRAFT) return properties.entrySet().stream();
                                    val namespaceLang = new HashMap<>();
                                    properties.forEach((key, value) -> {
                                        if (key instanceof String string) {
                                            String[] strings = string.split("\\.");
                                            if (strings.length > 1)
                                                strings[1] = langNs + ":" + strings[1];
                                            key = String.join(".", strings);
                                        }
                                        namespaceLang.put(key, value);
                                    });
                                    return namespaceLang.entrySet().stream();
                                })
                        )
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> newValue
                ));
        profiler.pop();

        profiler.endTick();
        return result;
    }

    @Override
    protected void apply(Map<Object, Object> prepared, ResourceManager manager, Profiler profiler) {
        profiler.startTick();
        profiler.push("upload");
        Properties translations = ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations();
        translations.clear();
        translations.putAll(prepared);
        profiler.swap("invalidate");
        StationAPI.EVENT_BUS.post(TranslationInvalidationEvent.builder().build());
        profiler.pop();
        profiler.endTick();
    }

    @Override
    public Identifier getId() {
        return LANGUAGES;
    }
}

