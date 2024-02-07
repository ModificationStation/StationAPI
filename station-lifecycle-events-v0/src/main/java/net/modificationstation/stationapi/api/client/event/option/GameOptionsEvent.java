package net.modificationstation.stationapi.api.client.event.option;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.client.option.GameOptions;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class GameOptionsEvent extends Event {
    public final GameOptions gameOptions;

    @SuperBuilder
    public static final class Load extends GameOptionsEvent {}

    @SuperBuilder
    public static final class Save extends GameOptionsEvent {}
}
