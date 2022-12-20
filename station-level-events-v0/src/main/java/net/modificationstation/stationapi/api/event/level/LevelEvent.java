package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.level.Level;

@SuperBuilder
public abstract class LevelEvent extends Event {

    public final Level level;

    @SuperBuilder
    public static class Init extends LevelEvent {

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
