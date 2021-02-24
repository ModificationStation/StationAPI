package net.modificationstation.stationapi.api.common.event.level;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelPropertiesEvent extends Event {

    public final LevelProperties levelProperties;
    public final CompoundTag tag;

    public static class Load extends LevelPropertiesEvent {

        public Load(LevelProperties levelProperties, CompoundTag tag) {
            super(levelProperties, tag);
        }
    }

    public static class Save extends LevelPropertiesEvent {

        public final CompoundTag spPlayerData;

        public Save(LevelProperties levelProperties, CompoundTag tag, CompoundTag spPlayerData) {
            super(levelProperties, tag);
            this.spPlayerData = spPlayerData;
        }
    }

    public static class LoadOnLevelInit extends LevelPropertiesEvent {

        public LoadOnLevelInit(LevelProperties levelProperties, CompoundTag tag) {
            super(levelProperties, tag);
        }
    }
}
