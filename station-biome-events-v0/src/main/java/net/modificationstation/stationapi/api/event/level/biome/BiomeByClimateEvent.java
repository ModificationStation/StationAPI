package net.modificationstation.stationapi.api.event.level.biome;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.level.biome.Biome;

@SuperBuilder
public class BiomeByClimateEvent extends Event {
    public final float temperature;
    public final float rainfall;
    public Biome currentBiome;
}
