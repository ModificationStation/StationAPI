package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.level.biome.Biome;

import java.util.Collection;

public interface BiomeProvider {
    /**
     * Biome provider method that return {@link Biome} based on conditions
     *
     * @param x           block X position
     * @param z           block Z position
     * @param temperature temperature at position
     * @param wetness     wetness (rainfall) at position
     * @return {@link Biome}
     */
    Biome getBiome(int x, int z, float temperature, float wetness);

    Collection<Biome> getBiomes();
    
    default void setSeed(long seed) {}
}
