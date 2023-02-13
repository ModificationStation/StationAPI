package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.level.dimension.McRegionDimensionFile;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.impl.level.dimension.FlattenedDimensionFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @SuppressWarnings({"InvalidInjectorMethodSignature", "InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget"})
    @Redirect(
            method = "prepareLevel",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/io/File;Ljava/lang/String;Z)Lnet/minecraft/level/dimension/McRegionDimensionFile;"
            )
    )
    private McRegionDimensionFile flatten(File file, String string, boolean bl) {
        return new FlattenedDimensionFile(file, string, bl);
    }
}
