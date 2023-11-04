package net.modificationstation.stationapi.api.event.world.biome;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.class_153;

@SuperBuilder
public class BiomeByClimateEvent extends Event {
    public final float temperature;
    public final float downfall;
    public class_153 currentBiome;
}
