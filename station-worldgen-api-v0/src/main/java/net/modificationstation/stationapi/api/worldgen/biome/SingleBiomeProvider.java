package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.class_153;

import java.util.Collection;
import java.util.Collections;

/**
 * Provides one biome independently on conditions
 */
public class SingleBiomeProvider implements BiomeProvider {
    private final class_153 biome;

    public SingleBiomeProvider(class_153 biome) {
        this.biome = biome;
    }

    @Override
    public class_153 getBiome(int x, int z, float temperature, float downfall) {
        return biome;
    }
    
    @Override
    public Collection<class_153> getBiomes() {
        return Collections.singleton(biome);
    }
}
