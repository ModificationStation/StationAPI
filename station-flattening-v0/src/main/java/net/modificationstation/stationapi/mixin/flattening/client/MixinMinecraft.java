package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.class_157;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.impl.level.storage.FlattenedWorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "init()V",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/io/File;)Lnet/minecraft/level/storage/McRegionLevelStorage;"
            )
    )
    private class_157 injectCustomLevelStorage(File saves) {
        return new FlattenedWorldStorage(saves);
    }
}
