package net.modificationstation.stationapi.api.common.event.level.biome;

import lombok.AllArgsConstructor;
import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.common.event.Event;

@AllArgsConstructor
public class BiomeByClimateEvent extends Event {

    public final float temperature;
    public final float rainfall;
    public Biome currentBiome;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
