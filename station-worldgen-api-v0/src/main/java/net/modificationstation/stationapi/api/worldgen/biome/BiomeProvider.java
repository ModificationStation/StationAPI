package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.class_153;

import java.util.Collection;

public interface BiomeProvider {
    /**
     * Biome provider method that return {@link class_153} based on conditions
     *
     * @param x           block X position
     * @param z           block Z position
     * @param temperature temperature at position
     * @param downfall     wetness (downfall) at position
     * @return {@link class_153}
     */
    class_153 getBiome(int x, int z, float temperature, float downfall);

    Collection<class_153> getBiomes();
    
    default void setSeed(long seed) {}
}
