package net.modificationstation.stationapi.impl.registry.sync;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;

public class RemapStateImpl<T> implements RegistryIdRemapEvent.RemapState<T> {
    private final Int2IntMap rawIdChangeMap;
    private final Int2ReferenceMap<Identifier> oldIdMap;
    private final Int2ReferenceMap<Identifier> newIdMap;

    public RemapStateImpl(Registry<T> registry, Int2ReferenceMap<Identifier> oldIdMap, Int2IntMap rawIdChangeMap) {
        this.rawIdChangeMap = rawIdChangeMap;
        this.oldIdMap = oldIdMap;
        this.newIdMap = new Int2ReferenceOpenHashMap<>();

        for (Int2IntMap.Entry entry : rawIdChangeMap.int2IntEntrySet()) {
            Identifier id = registry.getId(registry.get(entry.getIntValue()));
            newIdMap.put(entry.getIntValue(), id);
        }
    }

    @Override
    public Int2IntMap getRawIdChangeMap() {
        return rawIdChangeMap;
    }

    @Override
    public Identifier getIdFromOld(int oldRawId) {
        return oldIdMap.get(oldRawId);
    }

    @Override
    public Identifier getIdFromNew(int newRawId) {
        return newIdMap.get(newRawId);
    }
}
