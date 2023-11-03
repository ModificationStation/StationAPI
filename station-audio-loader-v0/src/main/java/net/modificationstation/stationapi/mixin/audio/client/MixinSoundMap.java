package net.modificationstation.stationapi.mixin.audio.client;

import net.minecraft.class_266;
import net.minecraft.class_267;
import net.modificationstation.stationapi.api.client.resource.CustomSoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(class_266.class)
public class MixinSoundMap implements CustomSoundMap {

    @Shadow public boolean isRandomSound;

    @Shadow private Map<String, List<class_267>> idToSounds;

    @Shadow private List<class_267> soundList;

    @Shadow public int count;

    @Override
    public class_267 putSound(String id, URL url) {
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
            this.idToSounds.put(id, new ArrayList<>());
        }
        class_267 var4 = new class_267(filename, url);

        this.idToSounds.get(id).add(var4);
        this.soundList.add(var4);
        ++this.count;
        return var4;
    }
}
