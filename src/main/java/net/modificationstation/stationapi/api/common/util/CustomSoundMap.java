package net.modificationstation.stationapi.api.common.util;

import java.net.URL;
import net.minecraft.client.sound.SoundEntry;

public interface CustomSoundMap {
    SoundEntry putSound(String id, URL url);
}
