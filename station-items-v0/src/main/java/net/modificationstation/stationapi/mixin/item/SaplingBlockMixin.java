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
    @Shadow public abstract void generate(World world, int x, int y, int z, Random random);

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            generate(world, x, y, z, world.random);
        }
        return true;
    }
}
