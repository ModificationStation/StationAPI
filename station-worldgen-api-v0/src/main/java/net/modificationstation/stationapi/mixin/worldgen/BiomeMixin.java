package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.class_288;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeColorProvider;
import net.modificationstation.stationapi.api.worldgen.biome.StationBiome;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(Biome.class)
@SuppressWarnings({"rawtypes", "unchecked"})
class BiomeMixin implements StationBiome {
    @Shadow private boolean hasRain;
    @Shadow private boolean hasSnow;
    @Shadow protected List spawnablePassive;
    @Shadow protected List spawnableMonsters;
    @Shadow protected List spawnableWaterCreatures;
    @Unique private BiomeColorProvider grassColor = BiomeColorsImpl.DEFAULT_GRASS_COLOR;
    @Unique private BiomeColorProvider leavesColor = BiomeColorsImpl.DEFAULT_LEAVES_COLOR;
    @Unique private BiomeColorProvider fogColor = BiomeColorsImpl.DEFAULT_FOG_COLOR;
    @Unique private final List<SurfaceRule> surfaceRules = new ArrayList<>();
    @Unique private final List<Feature> features = new ArrayList<>();
    @Unique private boolean noDimensionFeatures;
    @Unique private int minHeight = 40;
    @Unique private int maxHeight = 128;

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
    public void setGrassColorProvider(BiomeColorProvider provider) {
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
    public void applySurfaceRules(World world, int x, int y, int z, BlockState state) {
        for (SurfaceRule rule : surfaceRules) {
            if (rule.canApply(world, x, y, z, state)) {
                rule.apply(world, x, y, z);
                return;
            }
        }
    }

    @Override
    public boolean noSurfaceRules() {
        return surfaceRules.isEmpty();
    }

    @Override
    public void setPrecipitation(boolean precipitation) {
        this.hasRain = precipitation;
    }

    @Override
    public void setSnow(boolean snow) {
        this.hasSnow = snow;
    }

    @Override
    public void addPassiveEntity(Class<? extends Entity> entityClass, int rarity) {
        this.spawnablePassive.add(new class_288(entityClass, rarity));
    }

    @Override
    public void addHostileEntity(Class<? extends Entity> entityClass, int rarity) {
        this.spawnableMonsters.add(new class_288(entityClass, rarity));
    }

    @Override
    public void addWaterEntity(Class<? extends Entity> entityClass, int rarity) {
        this.spawnableWaterCreatures.add(new class_288(entityClass, rarity));
    }

    @Override
    public int getMinHeight() {
        return minHeight;
    }

    @Override
    public void setMinHeight(int height) {
        minHeight = height;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }
    
    @Override
    public List<Feature> getFeatures() {
        return features;
    }
    
    @Override
    public void setNoDimensionFeatures(boolean noDimensionFeatures) {
        this.noDimensionFeatures = noDimensionFeatures;
    }
    
    @Override
    public boolean isNoDimensionFeatures() {
        return noDimensionFeatures;
    }
}
