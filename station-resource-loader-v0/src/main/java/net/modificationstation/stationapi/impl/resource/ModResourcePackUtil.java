package net.modificationstation.stationapi.impl.resource;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.resource.ResourcePackActivationType;
import net.modificationstation.stationapi.api.resource.ResourceType;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

/**
 * Internal utilities for managing resource packs.
 */
public final class ModResourcePackUtil {
    private static final Gson GSON = new Gson();

    private ModResourcePackUtil() {}

    /**
     * Appends mod resource packs to the given list.
     *
     * @param packs   the resource pack list to append
     * @param type    the type of resource
     * @param subPath the resource pack sub path directory in mods, may be {@code null}
     */
    public static void appendModResourcePacks(List<ModResourcePack> packs, ResourceType type, @Nullable String subPath) {
        String priorityKey = String.format("station-resource-loader-v0:%s_priority", type.getDirectory());
        Object2ReferenceMap<String, ObjectSet<String>> lowerThan = new Object2ReferenceOpenHashMap<>();
        Object2ReferenceMap<String, ObjectSet<String>> higherThan = new Object2ReferenceOpenHashMap<>();
        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            ModMetadata metadata = container.getMetadata();
            if (metadata.getType().equals("builtin")) continue;
            String id = metadata.getId();
            if (metadata.containsCustomValue(priorityKey)) {
                CustomValue.CvObject priority = metadata.getCustomValue(priorityKey).getAsObject();
                updatePriorities(lowerThan, id, priority, "lowerThan");
                updatePriorities(higherThan, id, priority, "higherThan");
            }

            ModResourcePack pack = ModNioResourcePack.create(NAMESPACE.id(id), getName(metadata), container, subPath, type, ResourcePackActivationType.ALWAYS_ENABLED);

            if (pack != null) {
                packs.add(pack);
            }
        }
        packs.sort((pack1, pack2) -> {
            String id1 = pack1.getFabricModMetadata().getId();
            String id2 = pack2.getFabricModMetadata().getId();
            ObjectSet<String> s;
            return
                    lowerThan.containsKey(id1) && ((s = lowerThan.get(id1)).contains("*") || s.contains(id2)) ||
                            higherThan.containsKey(id2) && ((s = higherThan.get(id2)).contains("*") || s.contains(id1)) ? -1 :
                            higherThan.containsKey(id1) && ((s = higherThan.get(id1)).contains("*") || s.contains(id2)) ||
                                    lowerThan.containsKey(id2) && ((s = lowerThan.get(id2)).contains("*") || s.contains(id1)) ? 1 : 0;
        });
    }

    private static void updatePriorities(Object2ReferenceMap<String, ObjectSet<String>> setToUpdate, String id, CustomValue.CvObject priority, String keyToUpdate) {
        if (priority.containsKey(keyToUpdate)) {
            CustomValue v = priority.get(keyToUpdate);
            (
                    switch (v.getType()) {
                        case ARRAY ->
                                StreamSupport
                                        .stream(priority.get(keyToUpdate).getAsArray().spliterator(), false)
                                        .map(CustomValue::getAsString);
                        case STRING ->
                                Stream
                                        .of(v.getAsString());
                        default -> throw new IllegalStateException("Unexpected value: " + v.getType());
                    }
            )
                    .forEach(setToUpdate.computeIfAbsent(id, id1 -> new ObjectOpenHashSet<>())::add);
        }
    }

    public static boolean containsDefault(ModMetadata info, String filename) {
        return "pack.mcmeta".equals(filename);
    }

    public static InputStream openDefault(ModMetadata info, ResourceType type, String filename) {
        return switch (filename) {
            case "pack.mcmeta" -> {
                String description = Objects.requireNonNullElse(info.getName(), "");
                String metadata = serializeMetadata(type == ResourceType.CLIENT_RESOURCES ? 13 : 12, description);
                yield IOUtils.toInputStream(metadata, Charsets.UTF_8);
            }
            default -> null;
        };
    }

    public static String serializeMetadata(int packVersion, String description) {
        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", packVersion);
        pack.addProperty("description", description);
        JsonObject metadata = new JsonObject();
        metadata.add("pack", pack);
        return GSON.toJson(metadata);
    }

    public static String getName(ModMetadata info) {
        if (info.getName() != null) {
            return info.getName();
        } else {
//          return Text.translatable("pack.name.fabricMod", info.getId());
            return info.getId();
        }
    }
}