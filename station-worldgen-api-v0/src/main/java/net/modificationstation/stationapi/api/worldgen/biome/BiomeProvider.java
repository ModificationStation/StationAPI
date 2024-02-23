package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.world.biome.Biome;

import java.util.Collection;

public interface BiomeProvider {
    /**
     * Biome provider method that return {@link Biome} based on conditions
     *
     * @param x           block X position
     * @param z           block Z position
     * @param temperature temperature at position
     * @param downfall     wetness (downfall) at position
     * @return {@link Biome}
     */
    Biome getBiome(int x, int z, float temperature, float downfall);

    Collection<Biome> getBiomes();
    
    default void setSeed(long seed) {}
}
