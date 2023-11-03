package net.modificationstation.stationapi.impl.worldgen;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.Forest;
import net.minecraft.level.biome.Hell;
import net.minecraft.level.biome.Rainforest;
import net.minecraft.level.biome.Sky;
import net.minecraft.level.biome.SparseBiome;
import net.minecraft.level.biome.Swampland;
import net.minecraft.level.biome.Taiga;
import net.modificationstation.stationapi.api.worldgen.biome.ClimateBiomeProvider;

import java.util.Collection;
import java.util.Set;

public class OverworldBiomeProviderImpl extends ClimateBiomeProvider {
    private static final OverworldBiomeProviderImpl INSTANCE = new OverworldBiomeProviderImpl();

    private OverworldBiomeProviderImpl() {
    }

    @Override
    public Biome getBiome(int x, int z, float temperature, float wetness) {
        Biome biome = getBiome(temperature, wetness);
        return biome == null ? Biome.getBiome(temperature, wetness) : biome;
    }

    @Override
    protected Biome getBiome(float temperature, float wetness) {
        Biome biome = super.getBiome(temperature, wetness);
        return biome == null ? Biome.getBiome(temperature, wetness) : biome;
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
