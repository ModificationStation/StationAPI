package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

/**
 * Allows for multi-box collision in a single block space
 * This interface should be applied to classes that extend {@link Block} or its children
 * Takes precedent over {@link Block#getCollisionShape}
 * Takes precedent over provided boxes from {@link HasVoxelShape}
 * If not present, a block will use {@link HasVoxelShape},
 * If {@link HasVoxelShape} isn't present, {@link Block#getCollisionShape} will be used
 */
public interface HasCollisionVoxelShape {
    Box[] getCollisionVoxelShape(World world, int x, int y, int z);
}
