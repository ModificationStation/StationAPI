package net.modificationstation.stationapi.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.util.SoundMap;
import net.modificationstation.stationapi.api.common.resource.RecursiveReader;
import net.modificationstation.stationapi.api.common.util.CustomSoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
            for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
                ModMetadata stationMod = modContainer.getMetadata();
                Path basePath = Paths.get("/assets/" + stationMod.getId() + "/stationapi/sounds/" + channel);
                if (MixinSoundHelper.class.getResource(basePath.toString().replace("\\", "/")) != null) {
                    RecursiveReader recursiveReader = new RecursiveReader("/assets/" + stationMod.getId() + "/stationapi/sounds/" + channel, (filepath) -> filepath.endsWith(".ogg") || filepath.endsWith(".mp3"));
                    for (URL audioUrl : recursiveReader.read()) {
                        String audioID = audioUrl.toString().replace("\\", "/").split("/stationapi/sounds/" + channel)[1].replaceFirst("/", "");
                        ((CustomSoundMap) array).putSound(stationMod.getId() + ":" + audioID, audioUrl);
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
