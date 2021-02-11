package net.modificationstation.stationapi.api.common.event.level.biome;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface BiomeByClimateProvider {

    GameEventOld<BiomeByClimateProvider> EVENT = new GameEventOld<>(BiomeByClimateProvider.class,
            listeners ->
                    (currentBiome, temperature, rainfall) -> {
                        for (BiomeByClimateProvider listener : listeners)
                            currentBiome = listener.getBiome(currentBiome, temperature, rainfall);
                        return currentBiome;
                    },
            (Consumer<GameEventOld<BiomeByClimateProvider>>) biomeByClimateProvider ->
                    biomeByClimateProvider.register((currentBiome, temperature, rainfall) -> {
                        Data data = new Data(currentBiome, temperature, rainfall);
                        GameEventOld.EVENT_BUS.post(data);
                        return data.currentBiome;
                    })
    );

    Biome getBiome(Biome currentBiome, float temperature, float rainfall);

    final class Data extends GameEventOld.Data<BiomeByClimateProvider> {

        public final float temperature;
        public final float rainfall;
        public Biome currentBiome;

        private Data(Biome currentBiome, float temperature, float rainfall) {
            super(EVENT);
            this.currentBiome = currentBiome;
            this.temperature = temperature;
            this.rainfall = rainfall;
        }
    }
}
