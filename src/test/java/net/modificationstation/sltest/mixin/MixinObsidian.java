package net.modificationstation.sltest.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.ObsidianBlock;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.StationBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

@Mixin(ObsidianBlock.class)
public abstract class MixinObsidian implements StationBlock {
    @Override
    public boolean onBonemealUse(World level, int x, int y, int z, BlockState state) {
        level.setBlockState(x, y, z, Block.LOG.getDefaultState());
        System.out.println(x + " " + y + " " + z);
        return true;
    }
}
