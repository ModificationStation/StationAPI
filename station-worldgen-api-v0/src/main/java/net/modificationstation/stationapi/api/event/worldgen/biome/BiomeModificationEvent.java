package net.modificationstation.stationapi.api.event.worldgen.biome;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.class_153;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class BiomeModificationEvent extends Event {
    public final World world;
    public final class_153 biome;
}