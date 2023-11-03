package net.modificationstation.stationapi.mixin.audio.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.class_266;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.SoundManager;
import net.modificationstation.stationapi.api.client.resource.CustomSoundMap;
import net.modificationstation.stationapi.api.resource.RecursiveReader;
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

// TODO: look into refactoring this, although not necessary.
@Mixin(SoundManager.class)
public class MixinSoundHelper {

    @Shadow private class_266 sounds;

    @Shadow private class_266 streaming;

    @Shadow private class_266 music;

    @Environment(EnvType.CLIENT)
    private static void loadModAudio(class_266 array, String channel) {
        try {
            for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
                ModMetadata stationMod = modContainer.getMetadata();
                Path basePath = Paths.get("/assets/" + stationMod.getId() + "/stationapi/sounds/" + channel);
                if (MixinSoundHelper.class.getResource(basePath.toString().replace("\\", "/")) != null) {
                    RecursiveReader recursiveReader = new RecursiveReader("/assets/" + stationMod.getId() + "/stationapi/sounds/" + channel, (filepath) -> filepath.endsWith(".ogg") || filepath.endsWith(".mp3") || filepath.endsWith(".wav"));
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
        loadModAudio(this.sounds, "sound");
        loadModAudio(this.streaming, "streaming");
        loadModAudio(this.music, "music");
    }


}
