package net.modificationstation.stationapi.api.vanillafix.world;

import net.minecraft.block.entity.BlockEntity;

public interface StationChunkCacheWorld {
    void unloadBlockEntity(BlockEntity blockEntity);
}
