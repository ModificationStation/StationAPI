package net.modificationstation.stationapi.api.event.level;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;

@SuperBuilder
public abstract class LevelPropertiesEvent extends Event {

    public final LevelProperties levelProperties;
    public final CompoundTag tag;

    @SuperBuilder
    public static class Load extends LevelPropertiesEvent {

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    @SuperBuilder
    public static class Save extends LevelPropertiesEvent {

        public final CompoundTag spPlayerData;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    @SuperBuilder
    public static class LoadOnLevelInit extends LevelPropertiesEvent {

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
