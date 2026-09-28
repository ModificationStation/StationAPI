package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Optional;

/**
 * Treats raw IDs as physical IDs and introduces logical IDs that are able to override physical IDs.
 * <p>
 * Logical IDs are not remapped and can be negative.
 *
 * @param <ENTRY> the object stored in the registry
 */
public class SimpleLogicalRegistry<ENTRY> extends SimpleRegistry<ENTRY> implements MutableLogicalRegistry<ENTRY> {
    private final Int2ReferenceMap<RegistryEntry.Reference<ENTRY>> logicalIdToEntry = new Int2ReferenceOpenHashMap<>();
    private final Reference2IntMap<ENTRY> entryToLogicalId = new Reference2IntOpenHashMap<>();

    public SimpleLogicalRegistry(RegistryKey<? extends Registry<ENTRY>> key, Lifecycle lifecycle) {
        super(key, lifecycle);
    }

    public SimpleLogicalRegistry(RegistryKey<? extends Registry<ENTRY>> key, Lifecycle lifecycle, boolean intrusive) {
        super(key, lifecycle, intrusive);
    }

    @Override
    public Optional<RegistryEntry.Reference<ENTRY>> getEntryByLogicalId(int logicalId) {
        return Optional.ofNullable(logicalIdToEntry.get(logicalId)).or(() -> getEntry(logicalId));
    }

    @Override
    public int getLogicalId(ENTRY entry) {
        if (entryToLogicalId.containsKey(entry))
            return entryToLogicalId.getInt(entry);
        return getRawId(entry);
    }

    private void setLogicalId(int logicalId, RegistryEntry.Reference<ENTRY> ref) {
        if (logicalIdToEntry.containsKey(logicalId))
            Util.throwOrPause(new IllegalStateException("Adding duplicate logical ID '" + logicalId + "' to registry"));
        logicalIdToEntry.put(logicalId, ref);
        entryToLogicalId.put(ref.value(), logicalId);
    }

    @Override
    public RegistryEntry.Reference<ENTRY> set(int rawId, int logicalId, RegistryKey<ENTRY> registryKey, ENTRY value, Lifecycle lifecycle) {
        final var ref = set(rawId, registryKey, value, lifecycle);
        setLogicalId(logicalId, ref);
        return ref;
    }

    @Override
    public RegistryEntry.Reference<ENTRY> add(int logicalId, RegistryKey<ENTRY> registryKey, ENTRY value, Lifecycle lifecycle) {
        final var ref = add(registryKey, value, lifecycle);
        setLogicalId(logicalId, ref);
        return ref;
    }
}
