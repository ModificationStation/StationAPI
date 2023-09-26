package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeColorProvider;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.ColoredBiome;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Biome.class)
public class MixinBiome implements ColoredBiome {
	private BiomeColorProvider grassColor = BiomeColorsImpl.DEFAULT_GRASS_COLOR;
	private BiomeColorProvider leavesColor = BiomeColorsImpl.DEFAULT_LEAVES_COLOR;
	
	@Override
	public BiomeColorProvider getGrassColor() {
		return grassColor;
	}
	
	@Override
	public BiomeColorProvider getLeavesColor() {
		return leavesColor;
	}
	
	@Override
	public void setGrassColor(BiomeColorProvider provider) {
		grassColor = provider;
	}
	
	@Override
	public void setLeavesColor(BiomeColorProvider provider) {
		leavesColor = provider;
	}
}
