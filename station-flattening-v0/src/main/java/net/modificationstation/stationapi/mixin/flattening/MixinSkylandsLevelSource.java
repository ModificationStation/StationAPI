package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_208;
import net.minecraft.class_43;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_208.class)
public class MixinSkylandsLevelSource {

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "getChunk(II)Lnet/minecraft/level/chunk/Chunk;",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/level/Level;[BII)Lnet/minecraft/level/chunk/Chunk;"
            )
    )
    private class_43 redirectChunk(World world, byte[] tiles, int xPos, int zPos) {
        return new FlattenedChunk(world, xPos, zPos);
    }

    @Inject(
            method = "getChunk(II)Lnet/minecraft/level/chunk/Chunk;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/chunk/Chunk;generateHeightmap()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void populateChunk(int j, int par2, CallbackInfoReturnable<class_43> cir, byte[] tiles, class_43 chunk) {
        if (chunk instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles);
    }
}
