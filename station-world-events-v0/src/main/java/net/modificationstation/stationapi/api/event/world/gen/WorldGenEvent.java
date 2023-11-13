package net.modificationstation.stationapi.api.event.world.gen;

import lombok.experimental.SuperBuilder;
import net.minecraft.class_153;
import net.minecraft.class_51;
import net.modificationstation.stationapi.api.event.world.WorldEvent;

import java.util.Random;

@SuperBuilder
public abstract class WorldGenEvent extends WorldEvent {
    public final class_51 levelSource;

    @SuperBuilder
    public static class ChunkDecoration extends WorldGenEvent {
        public final class_153 biome;
        public final int x, z;
        public final Random random;
    }
}
