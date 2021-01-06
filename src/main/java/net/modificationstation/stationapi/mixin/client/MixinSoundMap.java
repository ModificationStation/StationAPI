package net.modificationstation.stationapi.mixin.client;

import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundMap;
import net.modificationstation.stationapi.api.common.util.CustomSoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(SoundMap.class)
public class MixinSoundMap implements CustomSoundMap {

    @Shadow public boolean isRandomSound;

    @Shadow private Map idToSounds;

    @Shadow private List soundList;

    @Shadow public int count;

    @Override
    public SoundEntry putSound(String id, URL url) {
        id = id.toLowerCase();
        if (id.length() - id.replace(".", "").length() != 1) {
            throw new RuntimeException("You MUST name your audio files with an extension, and with no extra dots or any spaces!\ne.g: \"wolf_bark.ogg\" is fine, but \"wolf_bark\", \"wolf.bark.ogg\" and \"wolf bark.ogg\" are not.\nFile name: \"" + id + "\"");
        }
        String filename = id;
        id = id.split("\\.")[0];
        id = id.replaceAll("/", ".");
        if (this.isRandomSound) {
            while(Character.isDigit(id.charAt(id.length() - 1))) {
                id = id.substring(0, id.length() - 1);
            }
        }
        if (!this.idToSounds.containsKey(id)) {
            this.idToSounds.put(id, new ArrayList());
        }
        SoundEntry var4 = new SoundEntry(filename, url);

        ((List) this.idToSounds.get(id)).add(var4);
        this.soundList.add(var4);
        ++this.count;
        return var4;
    }
}
