package net.modificationstation.stationapi.api.world.chunk.light;

import net.minecraft.level.LightType;

public class BlockLightStorage
extends LightStorage<BlockLightStorage.Data> {
    protected BlockLightStorage(ChunkProvider chunkProvider) {
        super(LightType.field_2758, chunkProvider, new Data(new Long2ObjectOpenHashMap<ChunkNibbleArray>()));
    }

    @Override
    protected int getLight(long blockPos) {
        long l = ChunkSectionPos.fromBlockPos(blockPos);
        ChunkNibbleArray chunkNibbleArray = this.getLightSection(l, false);
        if (chunkNibbleArray == null) {
            return 0;
        }
        return chunkNibbleArray.get(ChunkSectionPos.getLocalCoord(BlockPos.unpackLongX(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongY(blockPos)), ChunkSectionPos.getLocalCoord(BlockPos.unpackLongZ(blockPos)));
    }

    protected static final class Data
    extends ChunkToNibbleArrayMap<Data> {
        public Data(Long2ObjectOpenHashMap<ChunkNibbleArray> long2ObjectOpenHashMap) {
            super(long2ObjectOpenHashMap);
        }

        @Override
        public Data copy() {
            return new Data((Long2ObjectOpenHashMap<ChunkNibbleArray>)this.arrays.clone());
        }
    }
}

