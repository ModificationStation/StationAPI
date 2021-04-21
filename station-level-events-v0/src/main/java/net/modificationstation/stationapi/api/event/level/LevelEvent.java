package net.modificationstation.stationapi.api.event.level;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.level.Level;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class LevelEvent extends Event {

    public final Level level;

    public static class Init extends LevelEvent {

        public Init(Level level) {
            super(level);
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
