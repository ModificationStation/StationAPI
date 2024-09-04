package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_344;
import net.minecraft.class_359;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_359.class)
class NetherChunkGeneratorMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/class_344;"
            )
    )
    private class_344 stationapi_setWorldForCaveGen(Operation<class_344> original, World world, long l) {
        final class_344 caveGen = original.call();
        ((CaveGenBaseImpl) caveGen).stationapi_setWorld(world);
        return caveGen;
    }

    @Redirect(
            method = "method_1806",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/World;[BII)Lnet/minecraft/world/chunk/Chunk;"
            )
    )
    private Chunk stationapi_redirectChunk(World world, byte[] tiles, int xPos, int zPos) {
        return new FlattenedChunk(world, xPos, zPos);
    }

    @Inject(
            method = "method_1806",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_populateChunk(int j, int par2, CallbackInfoReturnable<Chunk> cir, byte[] tiles) {
        if (cir.getReturnValue() instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles);
    }
}
