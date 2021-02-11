package net.modificationstation.stationapi.api.common.event.level.gen;

import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.Random;
import java.util.function.Consumer;

public interface ChunkPopulator {

    GameEventOld<ChunkPopulator> EVENT = new GameEventOld<>(ChunkPopulator.class,
            listeners ->
                    (level, levelSource, biome, x, z, random) -> {
                        for (ChunkPopulator listener : listeners)
                            listener.populate(level, levelSource, biome, x, z, random);
                    },
            (Consumer<GameEventOld<ChunkPopulator>>) chunkPopulator ->
                    chunkPopulator.register((level, levelSource, biome, x, z, random) -> GameEventOld.EVENT_BUS.post(new Data(level, levelSource, biome, x, z, random)))
    );

    void populate(Level level, LevelSource levelSource, Biome biome, int x, int z, Random random);

    final class Data extends GameEventOld.Data<ChunkPopulator> {

        public final Level level;
        public final LevelSource levelSource;
        public final Biome biome;
        public final int x;
        public final int z;
        public final Random random;

        private Data(Level level, LevelSource levelSource, Biome biome, int x, int z, Random random) {
            super(EVENT);
            this.level = level;
            this.levelSource = levelSource;
            this.biome = biome;
            this.x = x;
            this.z = z;
            this.random = random;
        }
    }
}
