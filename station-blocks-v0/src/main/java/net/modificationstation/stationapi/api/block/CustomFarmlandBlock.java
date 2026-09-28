package net.modificationstation.stationapi.api.block;

import net.minecraft.world.World;

public interface CustomFarmlandBlock {
    boolean isWet(World world, int x, int y, int z);
}
