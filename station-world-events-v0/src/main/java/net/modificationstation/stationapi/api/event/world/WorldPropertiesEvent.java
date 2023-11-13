package net.modificationstation.stationapi.api.event.world;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
public abstract class WorldPropertiesEvent extends Event {
    public final WorldProperties worldProperties;
    public final NbtCompound nbt;

    @SuperBuilder
    public static class Load extends WorldPropertiesEvent {}

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class Save extends WorldPropertiesEvent {
        public final NbtCompound spPlayerNbt;
    }

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class LoadOnWorldInit extends WorldPropertiesEvent {}
}
