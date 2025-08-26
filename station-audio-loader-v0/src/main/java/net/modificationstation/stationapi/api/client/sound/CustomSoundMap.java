package net.modificationstation.stationapi.api.client.sound;

import java.net.URL;
import net.minecraft.client.sound.Sound;

public interface CustomSoundMap {
    Sound putSound(String id, URL url);
}
