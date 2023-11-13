package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.class_153;
import net.minecraft.class_239;
import net.minecraft.class_288;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
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

@Mixin(class_153.class)
@SuppressWarnings({"rawtypes", "unchecked"})
class BiomeMixin implements StationBiome {
    @Shadow private boolean field_897;
    @Shadow private boolean field_896;
    @Shadow protected List field_894;
    @Shadow protected List field_893;
    @Shadow protected List field_895;
    @Unique private BiomeColorProvider grassColor = BiomeColorsImpl.DEFAULT_GRASS_COLOR;
    @Unique private BiomeColorProvider leavesColor = BiomeColorsImpl.DEFAULT_LEAVES_COLOR;
    @Unique private BiomeColorProvider fogColor = BiomeColorsImpl.DEFAULT_FOG_COLOR;
    @Unique private final List<SurfaceRule> surfaceRules = new ArrayList<>();
    @Unique private final List<class_239> features = new ArrayList<>();
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
        this.field_897 = precipitation;
    }

    @Override
    public void setSnow(boolean snow) {
        this.field_896 = snow;
    }

    @Override
    public void addPassiveEntity(Class<? extends Entity> entityClass, int rarity) {
        this.field_894.add(new class_288(entityClass, rarity));
    }

    @Override
    public void addHostileEntity(Class<? extends Entity> entityClass, int rarity) {
        this.field_893.add(new class_288(entityClass, rarity));
    }

    @Override
    public void addWaterEntity(Class<? extends Entity> entityClass, int rarity) {
        this.field_895.add(new class_288(entityClass, rarity));
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
    public List<class_239> getFeatures() {
        return features;
    }
    
    @Override
    public void setNoDimensionFeatures(boolean noDimensionFeatures) {
        this.noDimensionFeatures = false;
    }
    
    @Override
    public boolean isNoDimensionFeatures() {
        return noDimensionFeatures;
    }
}
