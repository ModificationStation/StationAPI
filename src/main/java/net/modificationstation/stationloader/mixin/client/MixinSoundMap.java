package net.modificationstation.stationloader.mixin.client;

import net.minecraft.class_267;
import net.minecraft.util.SoundMap;
import net.modificationstation.stationloader.api.common.util.CustomSoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(SoundMap.class)
public class MixinSoundMap implements CustomSoundMap {
    @Shadow private Map field_1089;

    @Shadow private List field_1090;

    @Shadow public int field_1086;

    @Override
    public class_267 putSound(String id, URL url) {
        id = id.toLowerCase();
        if (id.length() - id.replace(".", "").length() != 1) {
            throw new RuntimeException("You MUST name your audio files with an extension, and with no extra dots or any spaces!\ne.g: \"wolf_bark.ogg\" is fine, but \"wolf_bark\", \"wolf.bark.ogg\" and \"wolf bark.ogg\" are not.\nFile name: \"" + id + "\"");
        }
        String filename = id;
        id = id.split("\\.")[0];
        id = id.replaceAll("/", ".");
        if (!this.field_1089.containsKey(id)) {
            this.field_1089.put(id, new ArrayList());
        }
        class_267 var4 = new class_267(filename, url);

        ((List)this.field_1089.get(id)).add(var4);
        this.field_1090.add(var4);
        ++this.field_1086;
        return var4;
    }
}
