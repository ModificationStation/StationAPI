package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.StatRegistry;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class StatRegistryEvent extends RegistryEvent.EntryTypeBound<Stat, StatRegistry> {
    public StatRegistryEvent() {
        super(StatRegistry.INSTANCE);
    }
}