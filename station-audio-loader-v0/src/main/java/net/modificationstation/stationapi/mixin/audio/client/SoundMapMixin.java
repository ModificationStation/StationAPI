package net.modificationstation.stationapi.mixin.audio.client;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundEntry;
import net.modificationstation.stationapi.api.client.sound.CustomSoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(SoundEntry.class)
class SoundMapMixin implements CustomSoundMap {
    @Shadow public boolean isRandom;

    @Shadow private Map<String, List<Sound>> weightedSoundSet;

    @Shadow private List<Sound> loadedSounds;

    @Shadow public int loadedSoundCount;

    @Override
    @Unique
    public Sound putSound(String id, URL url) {
        id = id.toLowerCase();
        if (id.length() - id.replace(".", "").length() != 1) {
            throw new RuntimeException("You MUST name your audio files with an extension, and with no extra dots or any spaces!\ne.g: \"wolf_bark.ogg\" is fine, but \"wolf_bark\", \"wolf.bark.ogg\" and \"wolf bark.ogg\" are not.\nFile name: \"" + id + "\"");
        }
        String filename = id;
        id = id.split("\\.")[0];
        id = id.replaceAll("/", ".");
        if (this.isRandom) {
            while(Character.isDigit(id.charAt(id.length() - 1))) {
                id = id.substring(0, id.length() - 1);
            }
        }
        if (!this.weightedSoundSet.containsKey(id)) {
            this.weightedSoundSet.put(id, new ArrayList<>());
        }
        Sound var4 = new Sound(filename, url);

        this.weightedSoundSet.get(id).add(var4);
        this.loadedSounds.add(var4);
        ++this.loadedSoundCount;
        return var4;
    }
}
