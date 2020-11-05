package net.modificationstation.stationloader.api.common.event.level.biome;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

import java.util.concurrent.atomic.AtomicReference;

public interface BiomeByClimateProvider {

    Event<BiomeByClimateProvider> EVENT = EventFactory.INSTANCE.newEvent(BiomeByClimateProvider.class, listeners ->
            (currentBiome, temperature, rainfall) -> {
                for (BiomeByClimateProvider event : listeners)
                    event.getBiome(currentBiome, temperature, rainfall);
            });

    void getBiome(AtomicReference<Biome> currentBiome, float temperature, float rainfall);
}
