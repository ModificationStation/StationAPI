package net.modificationstation.stationloader.api.common.event.level.gen;

import lombok.Getter;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.Random;
import java.util.function.Consumer;

public interface ChunkPopulator {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<ChunkPopulator> EVENT = new SimpleEvent<>(ChunkPopulator.class,
            listeners ->
                    (level, levelSource, biome, x, z, random) -> {
        for (ChunkPopulator listener : listeners)
            listener.populate(level, levelSource, biome, x, z, random);
    }, (Consumer<SimpleEvent<ChunkPopulator>>) chunkPopulator ->
            chunkPopulator.register((level, levelSource, biome, x, z, random) -> SimpleEvent.EVENT_BUS.post(new Data(level, levelSource, biome, x, z, random)))
    );

    void populate(Level level, LevelSource levelSource, Biome biome, int x, int z, Random random);

    final class Data extends SimpleEvent.Data<ChunkPopulator> {

        private Data(Level level, LevelSource levelSource, Biome biome, int x, int z, Random random) {
            super(EVENT);
            this.level = level;
            this.levelSource = levelSource;
            this.biome = biome;
            this.x = x;
            this.z = z;
            this.random = random;
        }

        @Getter
        private final Level level;
        @Getter
        private final LevelSource levelSource;
        @Getter
        private final Biome biome;
        @Getter
        private final int x;
        @Getter
        private final int z;
        @Getter
        private final Random random;
    }
}
