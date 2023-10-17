package net.modificationstation.stationapi.api.resource.language;

import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
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
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.profiler.DummyProfiler;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import net.modificationstation.stationapi.mixin.lang.TranslationStorageAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.*;
import static java.util.regex.Pattern.quote;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Environment(EnvType.CLIENT)
@Entrypoint(eventBus = @EventBusPolicy(registerStatic = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class LanguageManager implements IdentifiableResourceReloadListener {
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
        val tasks = new ConcurrentLinkedQueue<Runnable>();
        val reload = INSTANCE.reload(
                new Synchronizer() {
                    @Override
                    public <T> CompletableFuture<T> whenPrepared(T var1) {
                        return supplyAsync(() -> var1);
                    }
                },
                ReloadableAssetsManager.INSTANCE,
                DummyProfiler.INSTANCE,
                DummyProfiler.INSTANCE,
                tasks::add,
                tasks::add
        );
        while (!reload.isDone()) {
            val command = tasks.poll();
            if (command != null) command.run();
        }
        if (reload.isCompletedExceptionally()) reload.join();
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
    public CompletableFuture<Void> reload(
            Synchronizer synchronizer,
            ResourceManager manager,
            Profiler prepareProfiler,
            Profiler applyProfiler,
            Executor prepareExecutor,
            Executor applyExecutor
    ) {
        return runAsync(prepareProfiler::startTick, prepareExecutor)
                .thenApply(__ -> {
                    val resources = new Reference2ObjectOpenHashMap<ModID, Reference2ObjectMap<Identifier, List<Resource>>>();
                    LANG_PATHS.forEach((path, namespace) ->
                            resources.computeIfAbsent(
                                    namespace,
                                    key -> new Reference2ObjectOpenHashMap<>()
                            ).putAll(manager.findAllResources(path, identifier -> pathPredicate.test(identifier.id)))
                    );
                    return resources;
                })
                .thenCompose(map -> read(map, prepareExecutor, prepareProfiler))
                .thenApply(map -> {
                    prepareProfiler.endTick();
                    return map;
                })
                .thenCompose(synchronizer::whenPrepared)
                .thenAcceptAsync(
                        properties -> {
                            applyProfiler.startTick();
                            applyProfiler.push("put");
                            Properties translations = ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations();
                            translations.clear();
                            translations.putAll(properties);
                            applyProfiler.swap("invalidate");
                            StationAPI.EVENT_BUS.post(TranslationInvalidationEvent.builder().build());
                            applyProfiler.pop();
                            applyProfiler.endTick();
                        },
                        applyExecutor
                );
    }

    private static CompletableFuture<Map<Object, Object>> read(
            Reference2ObjectMap<ModID, Reference2ObjectMap<Identifier, List<Resource>>> langs,
            Executor prepareExecutor,
            Profiler prepareProfiler
    ) {
        prepareProfiler.push("read");
        val list = langs.entrySet()
                .stream()
                .flatMap(namespaceEntry -> namespaceEntry.getValue().entrySet()
                        .stream()
                        .flatMap(resourceEntry -> resourceEntry.getValue()
                                .stream()
                                .map(resource -> supplyAsync(() -> {
                                    val properties = new Properties();
                                    try (InputStream stream = resource.getInputStream()) {
                                        properties.load(stream);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    val namespace = namespaceEntry.getKey() == null ? resourceEntry.getKey().modID : namespaceEntry.getKey();
                                    if (namespace == ModID.MINECRAFT) return properties;
                                    val namespaceLang = new HashMap<>();
                                    properties.forEach((key, value) -> {
                                        if (key instanceof String string) {
                                            String[] strings = string.split("\\.");
                                            if (strings.length > 1)
                                                strings[1] = namespace + ":" + strings[1];
                                            key = String.join(".", strings);
                                        }
                                        namespaceLang.put(key, value);
                                    });
                                    return namespaceLang;
                                }, prepareExecutor))
                        )
                ).toList();
        return allOf(list.toArray(CompletableFuture[]::new))
                .thenApply(__ -> {
                    prepareProfiler.swap("combine");
                    val combined = list
                            .stream()
                            .map(CompletableFuture::join)
                            .flatMap(map -> map.entrySet().stream())
                            .distinct()
                            .collect(Collectors
                                    .<Map.Entry<Object, Object>, Object, Object>toMap(
                                            Map.Entry::getKey,
                                            Map.Entry::getValue
                                    )
                            );
                    prepareProfiler.pop();
                    return combined;
                });
    }

    @Override
    public Identifier getId() {
        return LANGUAGES;
    }
}

