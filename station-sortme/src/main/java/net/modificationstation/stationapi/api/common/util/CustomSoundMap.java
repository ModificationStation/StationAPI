package net.modificationstation.stationapi.api.common.util;

import net.minecraft.client.sound.SoundEntry;

public interface CustomSoundMap {
    SoundEntry putSound(String id, URL url);
}
