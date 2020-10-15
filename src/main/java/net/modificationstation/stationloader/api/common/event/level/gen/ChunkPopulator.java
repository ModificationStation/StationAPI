package net.modificationstation.stationloader.api.common.event.level.gen;

import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

import java.util.Random;

public interface ChunkPopulator {

    Event<ChunkPopulator> EVENT = EventFactory.INSTANCE.newEvent(ChunkPopulator.class, (listeners) ->
            (level, levelSource, biome, x, z, random) -> {
                for (ChunkPopulator event : listeners)
                    event.populate(level, levelSource, biome, x, z, random);
            });

    void populate(Level level, LevelSource levelSource, Biome biome, int x, int z, Random random);
}
