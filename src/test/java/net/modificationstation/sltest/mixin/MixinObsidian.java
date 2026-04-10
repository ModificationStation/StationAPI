package net.modificationstation.sltest.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.ObsidianBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ObsidianBlock.class)
public abstract class MixinObsidian implements StationBlock {
    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        world.setBlockStateWithoutNotifyingNeighbors(x, y, z, Block.LOG.getDefaultState());
        System.out.println(x + " " + y + " " + z);
        return true;
    }
}
