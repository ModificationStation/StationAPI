package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
public abstract class LevelPropertiesEvent extends Event {
    public final LevelProperties levelProperties;
    public final CompoundTag tag;

    @SuperBuilder
    public static class Load extends LevelPropertiesEvent {}

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class Save extends LevelPropertiesEvent {
        public final CompoundTag spPlayerData;
    }

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class LoadOnLevelInit extends LevelPropertiesEvent {}
}
