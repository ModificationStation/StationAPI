package net.modificationstation.sltest.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasCollisionVoxelShape;
import net.modificationstation.stationapi.api.block.HasVoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FenceBlock.class)
abstract class FenceBlockMixin extends Block implements HasVoxelShape, HasCollisionVoxelShape {
    public FenceBlockMixin(int id, Material material) {
        super(id, material);
    }

    @Override
    public Box[] getVoxelShape(World world, int x, int y, int z) {
        int fenceId = Block.FENCE.id;
        boolean posX = world.getBlockId(x + 1, y, z) == fenceId;
        boolean negX = world.getBlockId(x - 1, y, z) == fenceId;
        boolean posZ = world.getBlockId(x, y, z + 1) == fenceId;
        boolean negZ = world.getBlockId(x, y, z - 1) == fenceId;
        int ct = 1;
        ct += posZ ? 1 : 0;
        ct += negZ ? 1 : 0;
        Box[] boxes = new Box[ct];
        boxes[0] = Box.create(x + (negX ? 0 : 0.375F), y + 0.F, z + 0.375F, x + (posX ? 1.F : 0.625F), y + 1.F, z + 0.625F);
        if (posZ) {
            boxes[1] = Box.createCached(x + 0.375F, y, z + 0.625F, x + 0.625F, y + 1, z + 1);
        }
        if (negZ) {
            int index = 1;
            if (posZ)
                index = 2;
            boxes[index] = Box.createCached(x + 0.375F, y, z, x + 0.625F, y + 1, z + 0.375F);
        }
        return boxes;
    }

    public Box[] getCollisionVoxelShape(World world, int x, int y, int z) {
        Box[] boxes = getVoxelShape(world, x, y, z);
        for (Box box : boxes) {
            box.maxY += .5;
        }
        return boxes;
    }
}
