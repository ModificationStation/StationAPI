package net.modificationstation.sltest.mixin;

import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasVoxelShape;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StairsBlock.class)
public class StairMixin implements HasVoxelShape {
    @Override
    public Box[] getVoxelShape(World world, int x, int y, int z) {
        int meta = world.getBlockMeta(x, y, z);
        if (meta < 0 || meta > 3) return new Box[0];
        return new Box[]{
                Box.createCached(x, y, z, x + 1, y + 0.5, z + 1),
                switch (meta) {
                    case 0 -> Box.createCached(x + .5, y + .5, z, x + 1, y + 1, z + 1);
                    case 1 -> Box.createCached(x, y + .5, z, x + .5, y + 1, z + 1);
                    case 2 -> Box.createCached(x, y + .5, z + .5, x + 1, y + 1, z + 1);
                    case 3 -> Box.createCached(x, y + .5, z, x + 1, y + 1, z + .5);
                    default -> throw new IllegalStateException("Unexpected stair meta: " + meta);
                }
        };
    }
}
