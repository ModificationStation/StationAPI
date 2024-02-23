package net.modificationstation.stationapi.api.worldgen.biome;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Provides biomes based on temperature/wetness(humidity) climate model, similar to default Minecraft behaviour
 */
public class ClimateBiomeProvider implements BiomeProvider {
    private final List<BiomeInfo> biomes = new ArrayList<>();

    /**
     * Adds a biome with climate parameters to the list
     *
     * @param biome {@link Biome} to add
     * @param t1    minimum temperature
     * @param t2    maximum temperature
     * @param d1    minimum wetness (downfall)
     * @param d2    maximum wetness (downfall)
     */
    public void addBiome(Biome biome, float t1, float t2, float d1, float d2) {
        biomes.add(new BiomeInfo(biome, t1, t2, d1, d2));
    }

    @Override
    public Biome getBiome(int x, int z, float temperature, float downfall) {
        Biome biome = getBiome(temperature, downfall);
        return biome == null ? biomes.get(0).biome : biome;
    }
    
    @Override
    public Collection<Biome> getBiomes() {
        Set<Biome> biomes = new ObjectOpenHashSet<>();
        this.biomes.forEach(info -> biomes.add(info.biome));
        return biomes;
    }
    
    protected Biome getBiome(float temperature, float wetness) {
        for (BiomeInfo info : biomes) {
            if (info.t1 > temperature || info.t2 < temperature) continue;
            if (info.d1 > wetness || info.d2 < wetness) continue;
            return info.biome;
        }
        return null;
    }

    private record BiomeInfo(
            Biome biome, float t1, float t2, float d1, float d2
    ) {}
}
