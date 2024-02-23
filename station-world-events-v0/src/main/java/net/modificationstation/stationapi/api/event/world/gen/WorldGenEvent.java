package net.modificationstation.stationapi.api.event.world.gen;

import lombok.experimental.SuperBuilder;
import net.minecraft.class_51;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.event.world.WorldEvent;

import java.util.Random;

@SuperBuilder
public abstract class WorldGenEvent extends WorldEvent {
    public final class_51 worldSource;

    @SuperBuilder
    public static class ChunkDecoration extends WorldGenEvent {
        public final Biome biome;
        public final int x, z;
        public final Random random;
    }
}
