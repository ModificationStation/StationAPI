package net.modificationstation.stationapi.mixin.item;

import net.minecraft.block.SaplingBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin implements StationBlock {
    @Shadow public abstract void method_533(World world, int x, int y, int z, Random random);

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            method_533(world, x, y, z, world.field_214);
        }
        return true;
    }
}
