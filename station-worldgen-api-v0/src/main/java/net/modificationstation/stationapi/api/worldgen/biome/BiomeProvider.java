package net.modificationstation.stationapi.api.worldgen.biome;

import java.util.Collection;
import net.minecraft.class_153;

public interface BiomeProvider {
    /**
     * Biome provider method that return {@link class_153} based on conditions
     *
     * @param x           block X position
     * @param z           block Z position
     * @param temperature temperature at position
     * @param wetness     wetness (rainfall) at position
     * @return {@link class_153}
     */
    class_153 getBiome(int x, int z, float temperature, float wetness);

    Collection<class_153> getBiomes();
    
    default void setSeed(long seed) {}
}
