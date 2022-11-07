/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.modificationstation.stationapi.api.world.chunk.light;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Arrays;

import net.minecraft.level.LightType;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;

public class SkyLightStorage
extends LightStorage<SkyLightStorage.Data> {
    private static final Direction[] LIGHT_REDUCTION_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};
    private final LongSet field_15820 = new LongOpenHashSet();
    private final LongSet sectionsToUpdate = new LongOpenHashSet();
    private final LongSet sectionsToRemove = new LongOpenHashSet();
    private final LongSet enabledColumns = new LongOpenHashSet();
    private volatile boolean hasUpdates;

    protected SkyLightStorage(ChunkProvider chunkProvider) {
        super(LightType.field_2757, chunkProvider, new Data(new Long2ObjectOpenHashMap<ChunkNibbleArray>(), new Long2IntOpenHashMap(), Integer.MAX_VALUE));
    }

    @Override
    protected int getLight(long blockPos) {
        return this.getLight(blockPos, false);
    }

    protected int getLight(long blockPos, boolean cached) {
        long l = ChunkSectionPos.fromBlockPos(blockPos);
        int i = ChunkSectionPos.unpackY(l);
        Data data = cached ? (Data)this.storage : (Data)this.uncachedStorage;
        int j = data.columnToTopSection.get(ChunkSectionPos.withZeroY(l));
        if (j == data.minSectionY || i >= j) {
            if (cached && !this.isSectionEnabled(l)) {
                return 0;
            }
            return 15;
        }
        ChunkNibbleArray chunkNibbleArray = this.getLightSection(data, l);
        if (chunkNibbleArray == null) {
            blockPos = BlockPos.removeChunkSectionLocalY(blockPos);
            while (chunkNibbleArray == null) {
                if (++i >= j) {
                    return 15;
                }
                blockPos = BlockPos.add(blockPos, 0, 16, 0);
                l = ChunkSectionPos.offset(l, Direction.UP);
                chunkNibbleArray = this.getLightSection(data, l);
            }
        }
        return chunkNibbleArray.get(ChunkSectionPos.getLocalCoord(BlockPos.unpackLongX(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongY(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongZ(blockPos)));
    }

    @Override
    protected void onLoadSection(long sectionPos) {
        long l;
        int j;
        int i = ChunkSectionPos.unpackY(sectionPos);
        if (((Data)this.storage).minSectionY > i) {
            ((Data)this.storage).minSectionY = i;
            ((Data)this.storage).columnToTopSection.defaultReturnValue(((Data)this.storage).minSectionY);
        }
        if ((j = ((Data)this.storage).columnToTopSection.get(l = ChunkSectionPos.withZeroY(sectionPos))) < i + 1) {
            ((Data)this.storage).columnToTopSection.put(l, i + 1);
            if (this.enabledColumns.contains(l)) {
                this.enqueueAddSection(sectionPos);
                if (j > ((Data)this.storage).minSectionY) {
                    long m = ChunkSectionPos.asLong(ChunkSectionPos.unpackX(sectionPos), j - 1, ChunkSectionPos.unpackZ(sectionPos));
                    this.enqueueRemoveSection(m);
                }
                this.checkForUpdates();
            }
        }
    }

    private void enqueueRemoveSection(long sectionPos) {
        this.sectionsToRemove.add(sectionPos);
        this.sectionsToUpdate.remove(sectionPos);
    }

    private void enqueueAddSection(long sectionPos) {
        this.sectionsToUpdate.add(sectionPos);
        this.sectionsToRemove.remove(sectionPos);
    }

    private void checkForUpdates() {
        this.hasUpdates = !this.sectionsToUpdate.isEmpty() || !this.sectionsToRemove.isEmpty();
    }

    @Override
    protected void onUnloadSection(long sectionPos) {
        long l = ChunkSectionPos.withZeroY(sectionPos);
        boolean bl = this.enabledColumns.contains(l);
        if (bl) {
            this.enqueueRemoveSection(sectionPos);
        }
        int i = ChunkSectionPos.unpackY(sectionPos);
        if (((Data)this.storage).columnToTopSection.get(l) == i + 1) {
            long m = sectionPos;
            while (!this.hasSection(m) && this.isAboveMinHeight(i)) {
                --i;
                m = ChunkSectionPos.offset(m, Direction.DOWN);
            }
            if (this.hasSection(m)) {
                ((Data)this.storage).columnToTopSection.put(l, i + 1);
                if (bl) {
                    this.enqueueAddSection(m);
                }
            } else {
                ((Data)this.storage).columnToTopSection.remove(l);
            }
        }
        if (bl) {
            this.checkForUpdates();
        }
    }

    @Override
    protected void setColumnEnabled(long columnPos, boolean enabled) {
        this.updateAll();
        if (enabled && this.enabledColumns.add(columnPos)) {
            int i = ((Data)this.storage).columnToTopSection.get(columnPos);
            if (i != ((Data)this.storage).minSectionY) {
                long l = ChunkSectionPos.asLong(ChunkSectionPos.unpackX(columnPos), i - 1, ChunkSectionPos.unpackZ(columnPos));
                this.enqueueAddSection(l);
                this.checkForUpdates();
            }
        } else if (!enabled) {
            this.enabledColumns.remove(columnPos);
        }
    }

    @Override
    protected boolean hasLightUpdates() {
        return super.hasLightUpdates() || this.hasUpdates;
    }

    @Override
    protected ChunkNibbleArray createSection(long sectionPos) {
        ChunkNibbleArray chunkNibbleArray2;
        ChunkNibbleArray chunkNibbleArray = (ChunkNibbleArray)this.queuedSections.get(sectionPos);
        if (chunkNibbleArray != null) {
            return chunkNibbleArray;
        }
        long l = ChunkSectionPos.offset(sectionPos, Direction.UP);
        int i = ((Data)this.storage).columnToTopSection.get(ChunkSectionPos.withZeroY(sectionPos));
        if (i == ((Data)this.storage).minSectionY || ChunkSectionPos.unpackY(l) >= i) {
            return new ChunkNibbleArray();
        }
        while ((chunkNibbleArray2 = this.getLightSection(l, true)) == null) {
            l = ChunkSectionPos.offset(l, Direction.UP);
        }
        return SkyLightStorage.copy(chunkNibbleArray2);
    }

    private static ChunkNibbleArray copy(ChunkNibbleArray source) {
        if (source.isUninitialized()) {
            return new ChunkNibbleArray();
        }
        byte[] bs = source.asByteArray();
        byte[] cs = new byte[2048];
        for (int i = 0; i < 16; ++i) {
            System.arraycopy(bs, 0, cs, i * 128, 128);
        }
        return new ChunkNibbleArray(cs);
    }

    @Override
    protected void updateLight(ChunkLightProvider<Data, ?> lightProvider, boolean doSkylight, boolean skipEdgeLightPropagation) {
        int j;
        int i;
        long l;
        LongIterator longIterator;
        super.updateLight(lightProvider, doSkylight, skipEdgeLightPropagation);
        if (!doSkylight) {
            return;
        }
        if (!this.sectionsToUpdate.isEmpty()) {
            longIterator = this.sectionsToUpdate.iterator();
            while (longIterator.hasNext()) {
                int k;
                l = (Long)longIterator.next();
                i = this.getLevel(l);
                if (i == 2 || this.sectionsToRemove.contains(l) || !this.field_15820.add(l)) continue;
                if (i == 1) {
                    long n;
                    this.removeSection(lightProvider, l);
                    if (this.dirtySections.add(l)) {
                        ((Data)this.storage).replaceWithCopy(l);
                    }
                    Arrays.fill(this.getLightSection(l, true).asByteArray(), (byte)-1);
                    j = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackX(l));
                    k = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackY(l));
                    int m = ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackZ(l));
                    for (Direction direction : LIGHT_REDUCTION_DIRECTIONS) {
                        n = ChunkSectionPos.offset(l, direction);
                        if (!this.sectionsToRemove.contains(n) && (this.field_15820.contains(n) || this.sectionsToUpdate.contains(n)) || !this.hasSection(n)) continue;
                        for (int o = 0; o < 16; ++o) {
                            for (int p = 0; p < 16; ++p) {
                                long q;
                                long r = switch (direction) {
                                    case Direction.NORTH -> {
                                        q = BlockPos.asLong(j + o, k + p, m);
                                        yield BlockPos.asLong(j + o, k + p, m - 1);
                                    }
                                    case Direction.SOUTH -> {
                                        q = BlockPos.asLong(j + o, k + p, m + 16 - 1);
                                        yield BlockPos.asLong(j + o, k + p, m + 16);
                                    }
                                    case Direction.WEST -> {
                                        q = BlockPos.asLong(j, k + o, m + p);
                                        yield BlockPos.asLong(j - 1, k + o, m + p);
                                    }
                                    default -> {
                                        q = BlockPos.asLong(j + 16 - 1, k + o, m + p);
                                        yield BlockPos.asLong(j + 16, k + o, m + p);
                                    }
                                };
                                lightProvider.updateLevel(q, r, lightProvider.getPropagatedLevel(q, r, 0), true);
                            }
                        }
                    }
                    for (int s = 0; s < 16; ++s) {
                        for (int t = 0; t < 16; ++t) {
                            long u = BlockPos.asLong(ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackX(l), s), ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackY(l)), ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackZ(l), t));
                            n = BlockPos.asLong(ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackX(l), s), ChunkSectionPos.getBlockCoord(ChunkSectionPos.unpackY(l)) - 1, ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackZ(l), t));
                            lightProvider.updateLevel(u, n, lightProvider.getPropagatedLevel(u, n, 0), true);
                        }
                    }
                    continue;
                }
                for (j = 0; j < 16; ++j) {
                    for (k = 0; k < 16; ++k) {
                        long v = BlockPos.asLong(ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackX(l), j), ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackY(l), 15), ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackZ(l), k));
                        lightProvider.updateLevel(Long.MAX_VALUE, v, 0, true);
                    }
                }
            }
        }
        this.sectionsToUpdate.clear();
        if (!this.sectionsToRemove.isEmpty()) {
            longIterator = this.sectionsToRemove.iterator();
            while (longIterator.hasNext()) {
                l = (Long)longIterator.next();
                if (!this.field_15820.remove(l) || !this.hasSection(l)) continue;
                for (i = 0; i < 16; ++i) {
                    for (j = 0; j < 16; ++j) {
                        long w = BlockPos.asLong(ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackX(l), i), ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackY(l), 15), ChunkSectionPos.getOffsetPos(ChunkSectionPos.unpackZ(l), j));
                        lightProvider.updateLevel(Long.MAX_VALUE, w, 15, false);
                    }
                }
            }
        }
        this.sectionsToRemove.clear();
        this.hasUpdates = false;
    }

    protected boolean isAboveMinHeight(int sectionY) {
        return sectionY >= ((Data)this.storage).minSectionY;
    }

    protected boolean isAtOrAboveTopmostSection(long sectionPos) {
        long l = ChunkSectionPos.withZeroY(sectionPos);
        int i = ((Data)this.storage).columnToTopSection.get(l);
        return i == ((Data)this.storage).minSectionY || ChunkSectionPos.unpackY(sectionPos) >= i;
    }

    protected boolean isSectionEnabled(long sectionPos) {
        long l = ChunkSectionPos.withZeroY(sectionPos);
        return this.enabledColumns.contains(l);
    }

    protected static final class Data
    extends ChunkToNibbleArrayMap<Data> {
        int minSectionY;
        final Long2IntOpenHashMap columnToTopSection;

        public Data(Long2ObjectOpenHashMap<ChunkNibbleArray> arrays, Long2IntOpenHashMap columnToTopSection, int minSectionY) {
            super(arrays);
            this.columnToTopSection = columnToTopSection;
            columnToTopSection.defaultReturnValue(minSectionY);
            this.minSectionY = minSectionY;
        }

        @Override
        public Data copy() {
            return new Data((Long2ObjectOpenHashMap<ChunkNibbleArray>)this.arrays.clone(), this.columnToTopSection.clone(), this.minSectionY);
        }
    }
}

