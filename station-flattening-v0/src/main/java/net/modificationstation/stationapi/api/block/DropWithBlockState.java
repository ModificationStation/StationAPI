package net.modificationstation.stationapi.api.block;

import net.minecraft.world.World;

public interface DropWithBlockState {

    default void drop(World level, int x, int y, int z, BlockState state, int meta) {
        dropWithChance(level, x, y, z, state, meta, 1);
    }

    void dropWithChance(World level, int x, int y, int z, BlockState state, int meta, float chance);
}
