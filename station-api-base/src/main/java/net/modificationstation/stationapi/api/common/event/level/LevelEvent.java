package net.modificationstation.stationapi.api.common.event.level;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.common.event.Event;

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
