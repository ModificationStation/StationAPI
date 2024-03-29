package net.modificationstation.stationapi.mixin.audio.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.class_266;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.SoundManager;
import net.modificationstation.stationapi.api.client.sound.CustomSoundMap;
import net.modificationstation.stationapi.api.resource.RecursiveReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
class SoundManagerMixin {
    @Shadow private class_266 field_2668;

    @Shadow private class_266 field_2669;

    @Shadow private class_266 field_2670;

    @Unique
    @Environment(EnvType.CLIENT)
    private static void stationapi_loadModAudio(class_266 array, String channel) {
        try {
            for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
                ModMetadata stationMod = modContainer.getMetadata();
                Path basePath = Paths.get("/assets/" + stationMod.getId() + "/stationapi/sounds/" + channel);
                if (SoundManagerMixin.class.getResource(basePath.toString().replace("\\", "/")) != null) {
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
    @Inject(
            method = "method_2012",
            at = @At("TAIL")
    )
    private void stationapi_loadModAudio(GameOptions paramkv, CallbackInfo ci) {
        stationapi_loadModAudio(this.field_2668, "sound");
        stationapi_loadModAudio(this.field_2669, "streaming");
        stationapi_loadModAudio(this.field_2670, "music");
    }
}
