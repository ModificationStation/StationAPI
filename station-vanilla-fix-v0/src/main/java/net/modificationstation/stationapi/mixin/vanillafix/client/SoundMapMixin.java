package net.modificationstation.stationapi.mixin.vanillafix.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;

@Mixin(SoundEntry.class)
public class SoundMapMixin {
    /**
     * Made because babric can dump .sha1 files inside workspaces, which partially breaks the sound system.
     */
    @Environment(EnvType.CLIENT)
    @Inject(
            method = "method_959",
            at = @At("HEAD"),
            cancellable = true
    )
    public void skipSha1(String file, File par2, CallbackInfoReturnable<Sound> cir) {
        if(file != null) {
            String[] splitFile = file.split("\\.");
            if (splitFile[splitFile.length - 1].equals("sha1")) {
                cir.setReturnValue(null);
            }
        }
    }
}
