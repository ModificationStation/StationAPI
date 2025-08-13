package net.modificationstation.stationapi.impl.registry.sync;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.*;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.network.packet.s2c.play.RemapClientRegistryS2CPacket;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public final class RegistrySyncManager {
    public static final boolean DEBUG = Boolean.getBoolean("stationapi.registry.debug");

    private static final boolean DEBUG_WRITE_REGISTRY_DATA = Boolean.getBoolean("stationapi.registry.debug.writeContentsAsCsv");

    //Set to true after registry's bootstrap has completed
    public static boolean postBootstrap = false;

    private RegistrySyncManager() {}

    @Environment(EnvType.SERVER)
    public static void configureClient(PlayerEntity player) {
        final Reference2ReferenceMap<Identifier, Reference2IntMap<Identifier>> map = RegistrySyncManager.createAndPopulateRegistryMap();

        // Don't send when there is nothing to map
        if (map == null) return;

        PacketHelper.sendTo(player, new RemapClientRegistryS2CPacket(map));
    }

    @Nullable
    @Environment(EnvType.SERVER)
    public static Reference2ReferenceMap<Identifier, Reference2IntMap<Identifier>> createAndPopulateRegistryMap() {
        Reference2ReferenceMap<Identifier, Reference2IntMap<Identifier>> map = new Reference2ReferenceLinkedOpenHashMap<>();

        for (Identifier registryId : Registries.REGISTRIES.getIds()) {
            Registry<?> registry = Registries.REGISTRIES.get(registryId);

            storeRegistry(map, registryId, registry);
        }

        if (map.isEmpty()) return null;

        return map;
    }

    @Environment(EnvType.SERVER)
    private static <T> void storeRegistry(Map<Identifier, Reference2IntMap<Identifier>> map, Identifier registryId, Registry<T> registry) {
        if (DEBUG_WRITE_REGISTRY_DATA) {
            File location = new File(".stationapi" + File.separatorChar + "debug" + File.separatorChar + "registry");
            boolean c = true;

            if (!location.exists()) if (!location.mkdirs()) {
                LOGGER.warn("[station-registry-api debug] Could not create " + location.getAbsolutePath() + " directory!");
                c = false;
            }

            if (c && registry != null) {
                File file = new File(location, registryId.toString().replace(':', '.').replace('/', '.') + ".csv");

                try (FileOutputStream stream = new FileOutputStream(file)) {
                    StringBuilder builder = new StringBuilder("Raw ID,String ID,Class Type\n");

                    for (T o : registry) {
                        String classType = (o == null) ? "null" : o.getClass().getName();
                        Identifier id = registry.getId(o);
                        if (id == null) continue;

                        int rawId = registry.getRawId(o);
                        String stringId = id.toString();
                        builder.append("\"").append(rawId).append("\",\"").append(stringId).append("\",\"").append(classType).append("\"\n");
                    }

                    stream.write(builder.toString().getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    LOGGER.warn("[station-registry-api debug] Could not write to " + file.getAbsolutePath() + "!", e);
                }
            }
        }

        RegistryAttributeHolder attributeHolder = RegistryAttributeHolder.get(registry.getKey());

        if (!attributeHolder.hasAttribute(RegistryAttribute.SYNCED)) {
            LOGGER.debug("Not syncing registry: {}", registryId);
            return;
        }

        /*
         * Dont do anything with vanilla registries on client sync.
         *
         * This will not sync IDs if a world has been previously modded, either from removed mods
         * or a previous version of stationapi registry sync.
         */
        if (!attributeHolder.hasAttribute(RegistryAttribute.MODDED)) {
            LOGGER.debug("Skipping un-modded registry: " + registryId);
            return;
        }

        LOGGER.debug("Syncing registry: " + registryId);

        if (registry instanceof RemappableRegistry) {
            Reference2IntMap<Identifier> idMap = new Reference2IntLinkedOpenHashMap<>();
            IntSet rawIdsFound = DEBUG ? new IntOpenHashSet() : null;

            for (T o : registry) {
                Identifier id = registry.getId(o);
                if (id == null) continue;

                int rawId = registry.getRawId(o);

                if (DEBUG) {
                    if (registry.get(id) != o)
                        LOGGER.error("[station-registry-api] Inconsistency detected in " + registryId + ": object " + o + " -> string ID " + id + " -> object " + registry.get(id) + "!");

                    if (registry.get(rawId) != o)
                        LOGGER.error("[station-registry-api] Inconsistency detected in " + registryId + ": object " + o + " -> integer ID " + rawId + " -> object " + registry.get(rawId) + "!");

                    if (!rawIdsFound.add(rawId))
                        LOGGER.error("[station-registry-api] Inconsistency detected in " + registryId + ": multiple objects hold the raw ID " + rawId + " (this one is " + id + ")");
                }

                idMap.put(id, rawId);
            }

            map.put(registryId, idMap);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void apply(Map<Identifier, Reference2IntMap<Identifier>> map, RemappableRegistry.RemapMode mode) throws RemapException {
        if (mode == RemappableRegistry.RemapMode.REMOTE) checkRemoteRemap(map);

        Set<Identifier> containedRegistries = Sets.newHashSet(map.keySet());

        for (Identifier registryId : Registries.REGISTRIES.getIds()) {
            if (!containedRegistries.remove(registryId)) continue;

            Reference2IntMap<Identifier> registryMap = map.get(registryId);
            Registry<?> registry = Registries.REGISTRIES.get(registryId);

            RegistryAttributeHolder attributeHolder = RegistryAttributeHolder.get(registry.getKey());

            if (!attributeHolder.hasAttribute(RegistryAttribute.MODDED)) {
                LOGGER.debug("Not applying registry data to vanilla registry {}", registryId.toString());
                continue;
            }

            if (registry instanceof RemappableRegistry) {
                Reference2IntMap<Identifier> idMap = new Reference2IntOpenHashMap<>();

                for (Identifier key : registryMap.keySet()) idMap.put(key, registryMap.getInt(key));

                ((RemappableRegistry) registry).remap(registryId.toString(), idMap, mode);
            }
        }

        if (!containedRegistries.isEmpty())
            LOGGER.warn("[station-registry-api] Could not find the following registries: " + Joiner.on(", ").join(containedRegistries));
    }

    @Environment(EnvType.CLIENT)
    public static void checkRemoteRemap(Map<Identifier, Reference2IntMap<Identifier>> map) throws RemapException {
        Map<Identifier, List<Identifier>> missingEntries = new HashMap<>();

        for (Map.Entry<? extends RegistryKey<? extends Registry<?>>, ? extends Registry<?>> entry : Registries.REGISTRIES.getEntrySet()) {
            final Registry<?> registry = entry.getValue();
            final Identifier registryId = entry.getKey().getValue();
            final Reference2IntMap<Identifier> remoteRegistry = map.get(registryId);

            // Registry sync does not contain data for this registry, will print a warning when applying.
            if (remoteRegistry == null) continue;

            // Found a registry entry from the server that is
            for (Identifier remoteId : remoteRegistry.keySet())
                if (!registry.containsId(remoteId))
                    missingEntries.computeIfAbsent(registryId, i -> new ArrayList<>()).add(remoteId);
        }

        // All good :)
        if (missingEntries.isEmpty()) return;

        // Print out details to the log
        LOGGER.error("Received unknown remote registry entries from server");

        for (Map.Entry<Identifier, List<Identifier>> entry : missingEntries.entrySet())
            for (Identifier identifier : entry.getValue())
                LOGGER.error("Registry entry ({}) is missing from local registry ({})", identifier, entry.getKey());

        // Create a nice user friendly error message.
        StringBuilder text = new StringBuilder();

        final int count = missingEntries.values().stream().mapToInt(List::size).sum();

        if (count == 1) text.append(I18n.getTranslation("station-registry-api-v0.unknown-remote.title.singular"));
        else text.append(I18n.getTranslation("station-registry-api-v0.unknown-remote.title.plural", count));

        text.append(Formatting.GREEN).append(I18n.getTranslation("station-registry-api-v0.unknown-remote.subtitle.1"));
        text.append(I18n.getTranslation("station-registry-api-v0.unknown-remote.subtitle.2"));

        final int toDisplay = 4;
        // Get the distinct missing namespaces
        final List<String> namespaces = missingEntries.values().stream()
                .flatMap(List::stream)
                .map(id -> id.namespace.toString())
                .distinct()
                .sorted()
                .toList();

        for (int i = 0; i < Math.min(namespaces.size(), toDisplay); i++) {
            text.append(Formatting.YELLOW).append(namespaces.get(i));
            text.append("\n");
        }

        if (namespaces.size() > toDisplay) {
            text.append(I18n.getTranslation("station-registry-api-v0.unknown-remote.footer", namespaces.size() - toDisplay));
        }

        throw new RemapException(text.toString());
    }

    @Environment(EnvType.CLIENT)
    public static void unmap() throws RemapException {
        for (Identifier registryId : Registries.REGISTRIES.getIds()) {
            Registry<?> registry = Registries.REGISTRIES.get(registryId);

            if (registry instanceof RemappableRegistry remappableRegistry)
                remappableRegistry.unmap(registryId.toString());
        }
    }

    public static void bootstrapRegistries() {
        postBootstrap = true;
    }
}