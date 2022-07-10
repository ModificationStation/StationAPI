package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.BlockBase;
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

    @SuperBuilder
    public static class IsBlockReplaceable extends LevelEvent {

        public final int x, y, z;
        public final BlockBase block;
        public final BlockBase replacedBy;
        public final int replacedByMeta;
        public boolean replace;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
