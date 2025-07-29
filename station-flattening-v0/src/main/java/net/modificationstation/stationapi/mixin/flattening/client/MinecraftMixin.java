package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.storage.RegionWorldStorageSource;
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
                    target = "(Ljava/io/File;)Lnet/minecraft/world/storage/RegionWorldStorageSource;"
            )
    )
    private RegionWorldStorageSource stationapi_injectCustomWorldStorage(File saves) {
        return new FlattenedWorldStorage(saves);
    }
}
