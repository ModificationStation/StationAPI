package net.modificationstation.stationapi.api.event.level;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.mine_diver.unsafeevents.Event;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class LevelPropertiesEvent extends Event {

    public final LevelProperties levelProperties;
    public final CompoundTag tag;

    public static class Load extends LevelPropertiesEvent {

        public Load(LevelProperties levelProperties, CompoundTag tag) {
            super(levelProperties, tag);
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class Save extends LevelPropertiesEvent {

        public final CompoundTag spPlayerData;

        public Save(LevelProperties levelProperties, CompoundTag tag, CompoundTag spPlayerData) {
            super(levelProperties, tag);
            this.spPlayerData = spPlayerData;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class LoadOnLevelInit extends LevelPropertiesEvent {

        public LoadOnLevelInit(LevelProperties levelProperties, CompoundTag tag) {
            super(levelProperties, tag);
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
