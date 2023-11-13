package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.class_157;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.impl.world.storage.FlattenedWorldStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(Minecraft.class)
class MinecraftMixin {
    @Redirect(
            method = "init",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/io/File;)Lnet/minecraft/class_157;"
            )
    )
    private class_157 stationapi_injectCustomWorldStorage(File saves) {
        return new FlattenedWorldStorage(saves);
    }
}
