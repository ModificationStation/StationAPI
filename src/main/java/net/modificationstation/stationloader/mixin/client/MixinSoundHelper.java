package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.util.SoundMap;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.util.CustomSoundMap;
import net.modificationstation.stationloader.impl.common.util.RecursiveReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mixin(SoundHelper.class)
public class MixinSoundHelper {
    @Shadow
    private SoundMap soundMapMusic;
    @Shadow
    private SoundMap soundMapSounds;
    @Shadow
    private SoundMap soundMapStreaming;

    @Environment(EnvType.CLIENT)
    private static void loadModAudio(SoundMap array, String channel) {
        try {
            for (ModMetadata stationMod : StationLoader.INSTANCE.getAllStationMods()) {
                Path basePath = Paths.get("/assets/" + stationMod.getId() + "/stationloader/sounds/" + channel);
                if (MixinSoundHelper.class.getResource(basePath.toString().replace("\\", "/")) != null) {
                    RecursiveReader recursiveReader = new RecursiveReader("/assets/" + stationMod.getId() + "/stationloader/sounds/" + channel, (filepath) -> filepath.endsWith(".ogg") || filepath.endsWith(".mp3"));
                    for (String audioPath : recursiveReader.read()) {
                        Path audioPathPath = Paths.get(audioPath);
                        ((CustomSoundMap) array).putSound(stationMod.getId() + ":" + basePath.relativize(audioPathPath).toString().replace("\\", "/"), MixinSoundHelper.class.getResource(audioPathPath.toString().replace("\\", "/")));
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "acceptOptions", at = @At(value = "TAIL"))
    private void loadModAudio(GameOptions paramkv, CallbackInfo ci) {
        // Cursed mappings has these completely wrong.
        loadModAudio(this.soundMapMusic, "sound");
        loadModAudio(this.soundMapSounds, "streaming");
        loadModAudio(this.soundMapStreaming, "music");
    }


}