package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.level.biome.Biome;

import java.util.Collection;
import java.util.Collections;

/**
 * Provides one biome independently on conditions
 */
public class SingleBiomeProvider implements BiomeProvider {
    private final Biome biome;

    public SingleBiomeProvider(Biome biome) {
        this.biome = biome;
    }

    @Override
    public Biome getBiome(int x, int z, float temperature, float wetness) {
        return biome;
    }
    
    @Override
    public Collection<Biome> getBiomes() {
        return Collections.singleton(biome);
    }
}
