package net.modificationstation.stationapi.api.event.entity.player;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResultType;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
public abstract class PlayerEvent extends Event {
    public final PlayerEntity player;

    @SuperBuilder
    @EventPhases(StationAPI.INTERNAL_PHASE)
    public static class Reach extends PlayerEvent {
        public final HitResultType type;
        public double currentReach;
    }
}
