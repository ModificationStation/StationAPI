package net.modificationstation.stationapi.api.common.event.level.gen;

import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;

import java.util.Random;

public class ChunkPopulator extends LevelGenEvent {

    public final Biome biome;
    public final int x, z;
    public final Random random;

    protected ChunkPopulator(Level level, LevelSource levelSource, Biome biome, int x, int z, Random random) {
        super(level, levelSource);
        this.biome = biome;
        this.x = x;
        this.z = z;
        this.random = random;
    }
}
