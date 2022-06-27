package net.modificationstation.stationapi.api.event.entity.player;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.hit.HitType;
import net.modificationstation.stationapi.api.entity.player.PlayerHandler;

import java.util.List;

@SuperBuilder
public abstract class PlayerEvent extends Event {

    public final PlayerBase player;

    @SuperBuilder
    public static class Reach extends PlayerEvent {

        public final HitType type;
        public double currentReach;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    @SuperBuilder
    public static class HandlerRegister extends PlayerEvent {

        public final List<PlayerHandler> playerHandlers;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
