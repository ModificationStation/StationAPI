package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

/**
 * Allows for multi-box collision/outline/ray calculation in a single block space
 * This interface should be applied to classes that extend {@link Block} or its children
 * Automatically adds to {@link Block#addIntersectingBoundingBox}
 * Takes precedent over {@link Block#getCollisionShape}
 * Takes precedent over {@link Block#setBoundingBox}
 * If different collision shape is needed, use {@link HasCollisionVoxelShape} in addition.
 * If {@link HasVoxelShape} isn't present, {@link Block#getCollisionShape} will be used
 */
public interface HasVoxelShape {
    Box[] getVoxelShape(World world, int x, int y, int z);
}
