package net.modificationstation.stationapi.api.event.level.gen;

import lombok.experimental.SuperBuilder;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.event.level.LevelEvent;

import java.util.Random;

@SuperBuilder
public abstract class LevelGenEvent extends LevelEvent {
    public final LevelSource levelSource;

    @SuperBuilder
    public static class ChunkDecoration extends LevelGenEvent {
        public final Biome biome;
        public final int x, z;
        public final Random random;
    }
}
