package net.modificationstation.stationapi.api.client.event.option;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class KeyBindingEvent extends Event {
    public final KeyBinding keyBinding;

    @Cancelable
    @SuperBuilder
    public static final class Save extends KeyBindingEvent {}

    @Cancelable
    @SuperBuilder
    public static final class Load extends KeyBindingEvent {}
}
