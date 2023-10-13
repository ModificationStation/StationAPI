package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.entity.EntityEntry;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeColorProvider;
import net.modificationstation.stationapi.api.worldgen.biome.StationBiome;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceRule;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

@Mixin(Biome.class)
@SuppressWarnings({"rawtypes", "unchecked"})
public class MixinBiome implements StationBiome {
    @Shadow private boolean precipitates;
    @Shadow private boolean snows;
    @Shadow protected List creatures;
    @Shadow protected List monsters;
    @Shadow protected List waterCreatures;
    @Unique private BiomeColorProvider grassColor = BiomeColorsImpl.DEFAULT_GRASS_COLOR;
    @Unique private BiomeColorProvider leavesColor = BiomeColorsImpl.DEFAULT_LEAVES_COLOR;
    @Unique private BiomeColorProvider fogColor = BiomeColorsImpl.DEFAULT_FOG_COLOR;
    @Unique private final List<SurfaceRule> surfaceRules = new ArrayList<>();
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

    @Override
    public void setPrecipitation(boolean precipitation) {
        this.precipitates = precipitation;
    }

    @Override
    public void setSnow(boolean snow) {
        this.snows = snow;
    }

    @Override
    public void addPassiveEntity(Class<? extends Entity> entityClass, int rarity) {
        this.creatures.add(new EntityEntry(entityClass, rarity));
    }

    @Override
    public void addHostileEntity(Class<? extends Entity> entityClass, int rarity) {
        this.monsters.add(new EntityEntry(entityClass, rarity));
    }

    @Override
    public void addWaterEntity(Class<? extends Entity> entityClass, int rarity) {
        this.waterCreatures.add(new EntityEntry(entityClass, rarity));
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
}
