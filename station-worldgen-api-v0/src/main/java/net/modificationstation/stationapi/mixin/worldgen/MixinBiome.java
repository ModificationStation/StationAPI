package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeColorProvider;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.ColoredBiome;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBiome;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(Biome.class)
public class MixinBiome implements ColoredBiome, SurfaceBiome {
	@Unique private BiomeColorProvider grassColor = BiomeColorsImpl.DEFAULT_GRASS_COLOR;
	@Unique private BiomeColorProvider leavesColor = BiomeColorsImpl.DEFAULT_LEAVES_COLOR;
	@Unique private BiomeColorProvider fogColor = BiomeColorsImpl.DEFAULT_FOG_COLOR;
	@Unique private final List<SurfaceRule> surfaceRules = new ArrayList<>();
	
	@Override
	public BiomeColorProvider getGrassColor() {
		return grassColor;
	}
	
	@Override
	public BiomeColorProvider getLeavesColor() {
		return leavesColor;
	}
	
	@Override
	public BiomeColorProvider getFogColor() {
		return fogColor;
	}
	
	@Override
	public void setGrassColor(BiomeColorProvider provider) {
		grassColor = provider;
	}
	
	@Override
	public void setLeavesColor(BiomeColorProvider provider) {
		leavesColor = provider;
	}
	
	@Override
	public void setFogColor(BiomeColorProvider provider) {
		fogColor = provider;
	}
	
	@Override
	public void addSurfaceRule(SurfaceRule rule) {
		surfaceRules.add(rule);
	}
	
	@Override
	public void applySurfaceRules(Level level, int x, int y, int z, BlockState state) {
		for (SurfaceRule rule : surfaceRules) {
			if (rule.canApply(level, x, y, z, state)) {
				rule.apply(level, x, y, z);
				return;
			}
		}
	}
	
	@Override
	public boolean noSurfaceRules() {
		return surfaceRules.isEmpty();
	}
}
