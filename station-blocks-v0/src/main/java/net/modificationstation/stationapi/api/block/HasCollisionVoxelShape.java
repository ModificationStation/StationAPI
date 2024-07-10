package net.modificationstation.stationapi.api.block;

import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public interface HasCollisionVoxelShape {
    Box[] getCollisionVoxelShape(World world, int x, int y, int z);
}
