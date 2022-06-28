package net.modificationstation.stationapi.api.event.tick;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;

@SuperBuilder
public abstract class GameTickEvent extends Event {

    @SuperBuilder
    public static final class End extends GameTickEvent {

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
