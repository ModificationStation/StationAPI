package net.modificationstation.stationapi.api.event.world.biome;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.world.biome.Biome;

@SuperBuilder
public class BiomeByClimateEvent extends Event {
    public final float temperature;
    public final float downfall;
    public Biome currentBiome;
}
