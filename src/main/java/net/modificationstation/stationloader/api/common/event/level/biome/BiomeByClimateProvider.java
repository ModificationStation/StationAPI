package net.modificationstation.stationloader.api.common.event.level.biome;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

public interface BiomeByClimateProvider {

    SimpleEvent<BiomeByClimateProvider> EVENT = new SimpleEvent<>(BiomeByClimateProvider.class,
            listeners ->
                    (currentBiome, temperature, rainfall) -> {
                        for (BiomeByClimateProvider listener : listeners)
                            currentBiome = listener.getBiome(currentBiome, temperature, rainfall);
                        return currentBiome;
                    },
            (Consumer<SimpleEvent<BiomeByClimateProvider>>) biomeByClimateProvider ->
                    biomeByClimateProvider.register((currentBiome, temperature, rainfall) -> {
                        Data data = new Data(currentBiome, temperature, rainfall);
                        //noinspection UnstableApiUsage
                        SimpleEvent.EVENT_BUS.post(data);
                        return data.getCurrentBiome();
                    })
    );

    Biome getBiome(Biome currentBiome, float temperature, float rainfall);

    final class Data extends SimpleEvent.Data<BiomeByClimateProvider> {

        @Getter
        private final float temperature;
        @Getter
        private final float rainfall;
        @Getter
        @Setter
        private Biome currentBiome;

        private Data(Biome currentBiome, float temperature, float rainfall) {
            super(EVENT);
            this.currentBiome = currentBiome;
            this.temperature = temperature;
            this.rainfall = rainfall;
        }
    }
}
