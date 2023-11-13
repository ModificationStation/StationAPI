package net.modificationstation.stationapi.api.block;

import net.minecraft.world.World;

public interface DropWithBlockState {

    default void drop(World world, int x, int y, int z, BlockState state, int meta) {
        dropWithChance(world, x, y, z, state, meta, 1);
    }

    void dropWithChance(World world, int x, int y, int z, BlockState state, int meta, float chance);
}
