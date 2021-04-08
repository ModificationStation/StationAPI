package net.modificationstation.stationapi.api.common.event.entity.player;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.common.entity.player.PlayerHandler;
import net.mine_diver.unsafeevents.Event;

import java.util.*;

@RequiredArgsConstructor
public abstract class PlayerEvent extends Event {

    public final PlayerBase player;

    public static class Reach extends PlayerEvent {

        public final HitType type;
        public double currentReach;

        public Reach(PlayerBase player, HitType type, double currentReach) {
            super(player);
            this.type = type;
            this.currentReach = currentReach;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class HandlerRegister extends PlayerEvent {

        public final List<PlayerHandler> playerHandlers;

        public HandlerRegister(PlayerBase player, List<PlayerHandler> playerHandlers) {
            super(player);
            this.playerHandlers = playerHandlers;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
