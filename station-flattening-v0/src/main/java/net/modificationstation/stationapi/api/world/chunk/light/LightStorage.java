package net.modificationstation.stationapi.api.world.chunk.light;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Iterator;

import net.minecraft.level.LightType;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.chunk.ChunkNibbleArray;
import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.jetbrains.annotations.Nullable;

/**
 * LightStorage handles the access, storage and propagation of a specific kind of light within the world.
 * For example, separate instances will be used to store block light as opposed to sky light.
 * 
 * <p>The smallest unit within LightStorage is the section. Sections represent a cube of 16x16x16 blocks and their lighting data.
 * In turn, 16 sections stacked on top of each other form a column, which are analogous to the standard 16x256x16 world chunks.
 * 
 * <p>To avoid allocations, LightStorage packs all the coordinate arguments into single long values. Extra care should be taken
 * to ensure that the relevant types are being used where appropriate.
 * 
 * @see SkyLightStorage
 * @see BlockLightStorage
 */
public abstract class LightStorage<M extends ChunkToNibbleArrayMap<M>>
extends SectionDistanceLevelPropagator {
    protected static final int field_31710 = 0;
    protected static final int field_31711 = 1;
    protected static final int field_31712 = 2;
    protected static final NibbleArray EMPTY = new NibbleArray();
    private static final Direction[] DIRECTIONS = Direction.values();
    private final LightType lightType;
    private final ChunkProvider chunkProvider;
    protected final LongSet readySections = new LongOpenHashSet();
    protected final LongSet markedNotReadySections = new LongOpenHashSet();
    protected final LongSet markedReadySections = new LongOpenHashSet();
    protected volatile M uncachedStorage;
    protected final M storage;
    protected final LongSet dirtySections = new LongOpenHashSet();
    protected final LongSet notifySections = new LongOpenHashSet();
    protected final Long2ObjectMap<NibbleArray> queuedSections = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
    private final LongSet queuedEdgeSections = new LongOpenHashSet();
    private final LongSet columnsToRetain = new LongOpenHashSet();
    private final LongSet sectionsToRemove = new LongOpenHashSet();
    protected volatile boolean hasLightUpdates;

    protected LightStorage(LightType lightType, ChunkProvider chunkProvider, M lightData) {
        super(3, 16, 256);
        this.lightType = lightType;
        this.chunkProvider = chunkProvider;
        this.storage = lightData;
        this.uncachedStorage = ((ChunkToNibbleArrayMap)lightData).copy();
        ((ChunkToNibbleArrayMap)this.uncachedStorage).disableCache();
    }

    protected boolean hasSection(long sectionPos) {
        return this.getLightSection(sectionPos, true) != null;
    }

    @Nullable
    protected NibbleArray getLightSection(long sectionPos, boolean cached) {
        return this.getLightSection(cached ? this.storage : this.uncachedStorage, sectionPos);
    }

    @Nullable
    protected NibbleArray getLightSection(M storage, long sectionPos) {
        return ((ChunkToNibbleArrayMap)storage).get(sectionPos);
    }

    @Nullable
    public NibbleArray getLightSection(long sectionPos) {
        NibbleArray chunkNibbleArray = (NibbleArray)this.queuedSections.get(sectionPos);
        if (chunkNibbleArray != null) {
            return chunkNibbleArray;
        }
        return this.getLightSection(sectionPos, false);
    }

    protected abstract int getLight(long var1);

    protected int get(long blockPos) {
        long l = ChunkSectionPos.fromBlockPos(blockPos);
        NibbleArray chunkNibbleArray = this.getLightSection(l, true);
        return chunkNibbleArray.getValue(ChunkNibbleArray.getIndex(ChunkSectionPos.getLocalCoord(BlockPos.unpackLongX(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongY(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongZ(blockPos))));
    }

    protected void set(long blockPos, int value) {
        long l = ChunkSectionPos.fromBlockPos(blockPos);
        if (this.dirtySections.add(l)) {
            ((ChunkToNibbleArrayMap)this.storage).replaceWithCopy(l);
        }
        NibbleArray chunkNibbleArray = this.getLightSection(l, true);
        chunkNibbleArray.setValue(ChunkSectionPos.getLocalCoord(BlockPos.unpackLongX(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongY(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongZ(blockPos)), value);
        ChunkSectionPos.forEachChunkSectionAround(blockPos, this.notifySections::add);
    }

    @Override
    protected int getLevel(long id) {
        if (id == Long.MAX_VALUE) {
            return 2;
        }
        if (this.readySections.contains(id)) {
            return 0;
        }
        if (!this.sectionsToRemove.contains(id) && ((ChunkToNibbleArrayMap)this.storage).containsKey(id)) {
            return 1;
        }
        return 2;
    }

    @Override
    protected int getInitialLevel(long id) {
        if (this.markedNotReadySections.contains(id)) {
            return 2;
        }
        if (this.readySections.contains(id) || this.markedReadySections.contains(id)) {
            return 0;
        }
        return 2;
    }

    @Override
    protected void setLevel(long id, int level) {
        int i = this.getLevel(id);
        if (i != 0 && level == 0) {
            this.readySections.add(id);
            this.markedReadySections.remove(id);
        }
        if (i == 0 && level != 0) {
            this.readySections.remove(id);
            this.markedNotReadySections.remove(id);
        }
        if (i >= 2 && level != 2) {
            if (this.sectionsToRemove.contains(id)) {
                this.sectionsToRemove.remove(id);
            } else {
                ((ChunkToNibbleArrayMap)this.storage).put(id, this.createSection(id));
                this.dirtySections.add(id);
                this.onLoadSection(id);
                int j = ChunkSectionPos.unpackX(id);
                int k = ChunkSectionPos.unpackY(id);
                int l = ChunkSectionPos.unpackZ(id);
                for (int m = -1; m <= 1; ++m) {
                    for (int n = -1; n <= 1; ++n) {
                        for (int o = -1; o <= 1; ++o) {
                            this.notifySections.add(ChunkSectionPos.asLong(j + n, k + o, l + m));
                        }
                    }
                }
            }
        }
        if (i != 2 && level >= 2) {
            this.sectionsToRemove.add(id);
        }
        this.hasLightUpdates = !this.sectionsToRemove.isEmpty();
    }

    protected NibbleArray createSection(long sectionPos) {
        NibbleArray chunkNibbleArray = (NibbleArray)this.queuedSections.get(sectionPos);
        if (chunkNibbleArray != null) {
            return chunkNibbleArray;
        }
        return new NibbleArray();
    }

    protected void removeSection(ChunkLightProvider<?, ?> storage, long sectionPos) {
        if (storage.getPendingUpdateCount() == 0) {
            return;
        }
        if (storage.getPendingUpdateCount() < 8192) {
            storage.removePendingUpdateIf(m -> ChunkSectionPos.fromBlockPos(m) == sectionPos);
            return;
        }
        int i = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackX(sectionPos));
        int j = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackY(sectionPos));
        int k = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackZ(sectionPos));
        for (int l = 0; l < 16; ++l) {
            for (int m2 = 0; m2 < 16; ++m2) {
                for (int n = 0; n < 16; ++n) {
                    long o = BlockPos.asLong(i + l, j + m2, k + n);
                    storage.removePendingUpdate(o);
                }
            }
        }
    }

    protected boolean hasLightUpdates() {
        return this.hasLightUpdates;
    }

    protected void updateLight(ChunkLightProvider<M, ?> lightProvider, boolean doSkylight, boolean skipEdgeLightPropagation) {
        long m;
        NibbleArray chunkNibbleArray2;
        long l;
        if (!this.hasLightUpdates() && this.queuedSections.isEmpty()) {
            return;
        }
        Iterator<Long> iterator = this.sectionsToRemove.iterator();
        while (iterator.hasNext()) {
            l = (Long)iterator.next();
            this.removeSection(lightProvider, l);
            NibbleArray chunkNibbleArray = (NibbleArray)this.queuedSections.remove(l);
            chunkNibbleArray2 = ((ChunkToNibbleArrayMap)this.storage).removeChunk(l);
            if (!this.columnsToRetain.contains(ChunkSectionPos.withZeroY(l))) continue;
            if (chunkNibbleArray != null) {
                this.queuedSections.put(l, chunkNibbleArray);
                continue;
            }
            if (chunkNibbleArray2 == null) continue;
            this.queuedSections.put(l, chunkNibbleArray2);
        }
        ((ChunkToNibbleArrayMap)this.storage).clearCache();
        iterator = this.sectionsToRemove.iterator();
        while (iterator.hasNext()) {
            l = (Long)iterator.next();
            this.onUnloadSection(l);
        }
        this.sectionsToRemove.clear();
        this.hasLightUpdates = false;
        for (Long2ObjectMap.Entry entry : this.queuedSections.long2ObjectEntrySet()) {
            m = entry.getLongKey();
            if (!this.hasSection(m)) continue;
            chunkNibbleArray2 = (NibbleArray)entry.getValue();
            if (((ChunkToNibbleArrayMap)this.storage).get(m) == chunkNibbleArray2) continue;
            this.removeSection(lightProvider, m);
            ((ChunkToNibbleArrayMap)this.storage).put(m, chunkNibbleArray2);
            this.dirtySections.add(m);
        }
        ((ChunkToNibbleArrayMap)this.storage).clearCache();
        if (!skipEdgeLightPropagation) {
            for (long l2 : this.queuedSections.keySet()) {
                this.updateSection(lightProvider, l2);
            }
        } else {
            for (long l3 : this.queuedEdgeSections) {
                this.updateSection(lightProvider, l3);
            }
        }
        this.queuedEdgeSections.clear();
        Iterator objectIterator = this.queuedSections.long2ObjectEntrySet().iterator();
        while (objectIterator.hasNext()) {
            Long2ObjectMap.Entry entry = (Long2ObjectMap.Entry)objectIterator.next();
            m = entry.getLongKey();
            if (!this.hasSection(m)) continue;
            objectIterator.remove();
        }
    }

    private void updateSection(ChunkLightProvider<M, ?> lightProvider, long sectionPos) {
        if (!this.hasSection(sectionPos)) {
            return;
        }
        int i = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackX(sectionPos));
        int j = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackY(sectionPos));
        int k = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackZ(sectionPos));
        for (Direction direction : DIRECTIONS) {
            long l = ChunkSectionPos.offset(sectionPos, direction);
            if (this.queuedSections.containsKey(l) || !this.hasSection(l)) continue;
            for (int m = 0; m < 16; ++m) {
                for (int n = 0; n < 16; ++n) {
                    long o;
                    long p = switch (direction) {
                        case Direction.DOWN -> {
                            o = BlockPos.asLong(i + n, j, k + m);
                            yield BlockPos.asLong(i + n, j - 1, k + m);
                        }
                        case Direction.UP -> {
                            o = BlockPos.asLong(i + n, j + 16 - 1, k + m);
                            yield BlockPos.asLong(i + n, j + 16, k + m);
                        }
                        case Direction.NORTH -> {
                            o = BlockPos.asLong(i + m, j + n, k);
                            yield BlockPos.asLong(i + m, j + n, k - 1);
                        }
                        case Direction.SOUTH -> {
                            o = BlockPos.asLong(i + m, j + n, k + 16 - 1);
                            yield BlockPos.asLong(i + m, j + n, k + 16);
                        }
                        case Direction.WEST -> {
                            o = BlockPos.asLong(i, j + m, k + n);
                            yield BlockPos.asLong(i - 1, j + m, k + n);
                        }
                        default -> {
                            o = BlockPos.asLong(i + 16 - 1, j + m, k + n);
                            yield BlockPos.asLong(i + 16, j + m, k + n);
                        }
                    };
                    lightProvider.updateLevel(o, p, lightProvider.getPropagatedLevel(o, p, lightProvider.getLevel(o)), false);
                    lightProvider.updateLevel(p, o, lightProvider.getPropagatedLevel(p, o, lightProvider.getLevel(p)), false);
                }
            }
        }
    }

    protected void onLoadSection(long sectionPos) {
    }

    protected void onUnloadSection(long sectionPos) {
    }

    protected void setColumnEnabled(long columnPos, boolean enabled) {
    }

    public void setRetainColumn(long sectionPos, boolean retain) {
        if (retain) {
            this.columnsToRetain.add(sectionPos);
        } else {
            this.columnsToRetain.remove(sectionPos);
        }
    }

    protected void enqueueSectionData(long sectionPos, @Nullable NibbleArray array, boolean nonEdge) {
        if (array != null) {
            this.queuedSections.put(sectionPos, array);
            if (!nonEdge) {
                this.queuedEdgeSections.add(sectionPos);
            }
        } else {
            this.queuedSections.remove(sectionPos);
        }
    }

    protected void setSectionStatus(long sectionPos, boolean notReady) {
        boolean bl = this.readySections.contains(sectionPos);
        if (!bl && !notReady) {
            this.markedReadySections.add(sectionPos);
            this.updateLevel(Long.MAX_VALUE, sectionPos, 0, true);
        }
        if (bl && notReady) {
            this.markedNotReadySections.add(sectionPos);
            this.updateLevel(Long.MAX_VALUE, sectionPos, 2, false);
        }
    }

    protected void updateAll() {
        if (this.hasPendingUpdates()) {
            this.applyPendingUpdates(Integer.MAX_VALUE);
        }
    }

    protected void notifyChanges() {
        if (!this.dirtySections.isEmpty()) {
            Object chunkToNibbleArrayMap = ((ChunkToNibbleArrayMap)this.storage).copy();
            ((ChunkToNibbleArrayMap)chunkToNibbleArrayMap).disableCache();
            this.uncachedStorage = chunkToNibbleArrayMap;
            this.dirtySections.clear();
        }
        if (!this.notifySections.isEmpty()) {
            LongIterator longIterator = this.notifySections.iterator();
            while (longIterator.hasNext()) {
                long l = longIterator.nextLong();
                this.chunkProvider.onLightUpdate(this.lightType, ChunkSectionPos.from(l));
            }
            this.notifySections.clear();
        }
    }
}

