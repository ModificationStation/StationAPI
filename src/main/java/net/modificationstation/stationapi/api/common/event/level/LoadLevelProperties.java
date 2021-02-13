package net.modificationstation.stationapi.api.common.event.level;

import lombok.RequiredArgsConstructor;
import net.minecraft.level.LevelProperties;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class LoadLevelProperties extends Event {

    public final LevelProperties levelProperties;
    public final CompoundTag tag;
}
