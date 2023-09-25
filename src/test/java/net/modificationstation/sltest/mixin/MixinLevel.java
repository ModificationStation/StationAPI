package net.modificationstation.sltest.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.noise.SimplexOctaveNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

@Mixin(Level.class)
public abstract class MixinLevel {
	@Shadow public abstract BiomeSource getBiomeSource();
	
	@Inject(
		method = "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;J)V",
		at = @At("TAIL")
	)
	private void onInit(DimensionData string, String l, long par3, CallbackInfo ci) {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
		
		int side = 800;
		
		BufferedImage buffer = new BufferedImage(side, side, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
		
		BiomeSource biomeSource = getBiomeSource();
		Biome[] biomes = biomeSource.getBiomes(new Biome[side * side], 0, 0, side, side);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = biomes[i].grassColour | 0xFF000000;
		}
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new JLabel(new ImageIcon(buffer)));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
