package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.RegionWorldStorage;
import net.modificationstation.stationapi.impl.world.dimension.FlattenedDimensionFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(MinecraftServer.class)
class MinecraftServerMixin {
    @Redirect(
            method = "loadWorld",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/io/File;Ljava/lang/String;Z)Lnet/minecraft/world/storage/RegionWorldStorage;"
            )
    )
    private RegionWorldStorage stationapi_flatten(File file, String string, boolean bl) {
        return new FlattenedDimensionFile(file, string, bl);
    }
}
