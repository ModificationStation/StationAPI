package net.modificationstation.stationapi.api.block;

import net.minecraft.level.Level;

public interface DropWithBlockState {

    default void drop(Level level, int x, int y, int z, BlockState state, int meta) {
        dropWithChance(level, x, y, z, state, meta, 1);
    }

    void dropWithChance(Level level, int x, int y, int z, BlockState state, int meta, float chance);
}
