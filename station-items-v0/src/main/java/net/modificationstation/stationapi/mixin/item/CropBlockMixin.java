package net.modificationstation.stationapi.mixin.item;

import net.minecraft.block.CropBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin implements StationBlock {

    @Shadow public abstract void applyFullGrowth(World world, int i, int j, int k);

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            applyFullGrowth(world, x, y, z); // Full grows crop.
        }
        return true;
    }
}
