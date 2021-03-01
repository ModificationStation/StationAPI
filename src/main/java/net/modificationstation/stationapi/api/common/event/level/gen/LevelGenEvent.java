package net.modificationstation.stationapi.api.common.event.level.gen;

import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.common.event.level.LevelEvent;

import java.util.*;

public class LevelGenEvent extends LevelEvent {

    public final LevelSource levelSource;

    protected LevelGenEvent(Level level, LevelSource levelSource) {
        super(level);
        this.levelSource = levelSource;
    }

    public static class ChunkDecoration extends LevelGenEvent {

        public final Biome biome;
        public final int x, z;
        public final Random random;

        public ChunkDecoration(Level level, LevelSource levelSource, Biome biome, int x, int z, Random random) {
            super(level, levelSource);
            this.biome = biome;
            this.x = x;
            this.z = z;
            this.random = random;
        }
    }
}
