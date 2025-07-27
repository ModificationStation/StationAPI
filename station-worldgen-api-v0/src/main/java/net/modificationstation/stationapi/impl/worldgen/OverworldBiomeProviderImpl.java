package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biome.ClimateBiomeProvider;

import java.util.Collection;

public class OverworldBiomeProviderImpl extends ClimateBiomeProvider {
    private static final OverworldBiomeProviderImpl INSTANCE = new OverworldBiomeProviderImpl();

    private OverworldBiomeProviderImpl() {
    }

    @Override
    public Biome getBiome(int x, int z, float temperature, float downfall) {
        Biome biome = getBiome(temperature, downfall);
        return biome == null ? Biome.getBiome(temperature, downfall) : biome;
    }

    @Override
    protected Biome getBiome(float temperature, float downfall) {
        Biome biome = super.getBiome(temperature, downfall);
        return biome == null ? Biome.getBiome(temperature, downfall) : biome;
    }
    
    @Override
    public Collection<Biome> getBiomes() {
        Collection<Biome> biomes = super.getBiomes();
        biomes.add(Biome.RAINFOREST);
        biomes.add(Biome.SWAMPLAND);
        biomes.add(Biome.SEASONAL_FOREST);
        biomes.add(Biome.FOREST);
        biomes.add(Biome.SAVANNA);
        biomes.add(Biome.SHRUBLAND);
        biomes.add(Biome.TAIGA);
        biomes.add(Biome.DESERT);
        biomes.add(Biome.PLAINS);
        biomes.add(Biome.ICE_DESERT);
        biomes.add(Biome.TUNDRA);
        return biomes;
    }

    public static OverworldBiomeProviderImpl getInstance() {
        return INSTANCE;
    }
}
