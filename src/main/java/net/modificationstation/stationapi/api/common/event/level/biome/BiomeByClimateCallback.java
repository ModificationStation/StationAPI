package net.modificationstation.stationapi.api.common.event.level.biome;

import lombok.RequiredArgsConstructor;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class BiomeByClimateCallback extends Event {

    public final float temperature;
    public final float rainfall;
    public Biome currentBiome;
}
