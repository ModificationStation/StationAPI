package net.modificationstation.sltest.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Obsidian;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.gen.BiomeSource;
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

@Mixin(Obsidian.class)
public abstract class MixinObsidian implements StationBlock {
    @Override
    public boolean onBonemealUse(Level level, int x, int y, int z, BlockState state) {
        level.setBlockState(x, y, z, BlockBase.LOG.getDefaultState());
        System.out.println(x + " " + y + " " + z);
        return true;
    }
}
