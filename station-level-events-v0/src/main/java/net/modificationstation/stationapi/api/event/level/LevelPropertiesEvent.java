package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
public abstract class LevelPropertiesEvent extends Event {
    public final WorldProperties levelProperties;
    public final NbtCompound tag;

    @SuperBuilder
    public static class Load extends LevelPropertiesEvent {}

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class Save extends LevelPropertiesEvent {
        public final NbtCompound spPlayerData;
    }

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class LoadOnLevelInit extends LevelPropertiesEvent {}
}
