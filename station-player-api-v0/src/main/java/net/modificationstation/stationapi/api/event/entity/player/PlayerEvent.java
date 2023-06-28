package net.modificationstation.stationapi.api.event.entity.player;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;

import java.util.List;

@SuperBuilder
public abstract class PlayerEvent extends Event {
    public final PlayerBase player;

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class Reach extends PlayerEvent {
        public final HitType type;
        public double currentReach;
    }

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class HandlerRegister extends PlayerEvent {
        public final List<PlayerHandler> playerHandlers;
    }
}
