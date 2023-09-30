package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;

import javax.swing.text.html.parser.Entity;

public interface StationBiome {
	default BiomeColorProvider getGrassColor() {
		return Util.assertImpl();
	}
	
	default BiomeColorProvider getLeavesColor() {
		return Util.assertImpl();
	}
	
	default BiomeColorProvider getFogColor() {
		return Util.assertImpl();
	}
	
	default void setGrassColor(BiomeColorProvider provider) {
		Util.assertImpl();
	}
	
	default void setLeavesColor(BiomeColorProvider provider) {
		Util.assertImpl();
	}
	
	default void setFogColor(BiomeColorProvider provider) {
		Util.assertImpl();
	}
	
	default void setGrassColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setGrassColor((source, x, z) -> color);
	}
	
	default void setLeavesColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setLeavesColor((source, x, z) -> color);
	}
	
	default void setFogColor(int rgb) {
		final int color = 0xFF000000 | rgb;
		setFogColor((source, x, z) -> color);
	}
	
	default void addSurfaceRule(SurfaceRule rule) {
		Util.assertImpl();
	}
	
	default void applySurfaceRules(Level level, int x, int y, int z, BlockState state) {
		Util.assertImpl();
	}
	
	default boolean noSurfaceRules() {
		return Util.assertImpl();
	}
	
	default void setPrecipitation(boolean precipitation) {
		Util.assertImpl();
	}
	
	default void setSnow(boolean snow) {
		Util.assertImpl();
	}
	
	default void addPassiveEntity(Class<? extends Entity> entityClass, int rarity) {
		Util.assertImpl();
	}
	
	default void addHostileEntity(Class<? extends Entity> entityClass, int rarity) {
		Util.assertImpl();
	}
	
	default void addWaterEntity(Class<? extends Entity> entityClass, int rarity) {
		Util.assertImpl();
	}
	
	default int getMinHeight() {
		return Util.assertImpl();
	}
	
	default void setMinHeight(int height) {
		Util.assertImpl();
	}
	
	default int getMaxHeight() {
		return Util.assertImpl();
	}
	
	default void setMaxHeight(int height) {
		Util.assertImpl();
	}
}
