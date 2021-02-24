package net.modificationstation.stationapi.api.common.event.entity.player;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.common.entity.player.PlayerHandler;
import net.modificationstation.stationapi.api.common.event.Event;

import java.util.List;

@RequiredArgsConstructor
public class PlayerEvent extends Event {

    public final PlayerBase player;

    public static class Reach extends PlayerEvent {

        public final HitType type;
        public double currentReach;

        public Reach(PlayerBase player, HitType type, double currentReach) {
            super(player);
            this.type = type;
            this.currentReach = currentReach;
        }
    }

    public static class HandlerRegister extends PlayerEvent {

        public final List<PlayerHandler> playerHandlers;

        public HandlerRegister(PlayerBase player, List<PlayerHandler> playerHandlers) {
            super(player);
            this.playerHandlers = playerHandlers;
        }
    }
}
