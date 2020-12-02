package net.modificationstation.stationloader.api.common.event.level.biome;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.concurrent.atomic.AtomicReference;

public interface BiomeByClimateProvider {

    SimpleEvent<BiomeByClimateProvider> EVENT = new SimpleEvent<>(BiomeByClimateProvider.class, listeners ->
            (currentBiome, temperature, rainfall) -> {
                for (BiomeByClimateProvider event : listeners)
                    event.getBiome(currentBiome, temperature, rainfall);
            });

    void getBiome(AtomicReference<Biome> currentBiome, float temperature, float rainfall);
}
