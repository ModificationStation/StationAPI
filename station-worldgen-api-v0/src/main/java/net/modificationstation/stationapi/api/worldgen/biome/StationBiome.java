package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;

import java.util.List;

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

    default void setGrassColorProvider(BiomeColorProvider provider) {
        Util.assertImpl();
    }

    default void setLeavesColor(BiomeColorProvider provider) {
        Util.assertImpl();
    }

    default void setFogColor(BiomeColorProvider provider) {
        Util.assertImpl();
    }

    default void setFixedGrassColorProvider(int rgb) {
        final int color = 0xFF000000 | rgb;
        setGrassColorProvider((source, x, z) -> color);
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

    default void applySurfaceRules(World world, int x, int y, int z, BlockState state) {
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
    
    default List<Feature> getFeatures() {
        return Util.assertImpl();
    }
    
    default void addFeature(Feature feature) {
        getFeatures().add(feature);
    }
    
    default void setNoDimensionFeatures(boolean noDimensionFeatures) {
        Util.assertImpl();
    }
    
    default boolean isNoDimensionFeatures() {
        return Util.assertImpl();
    }
}
