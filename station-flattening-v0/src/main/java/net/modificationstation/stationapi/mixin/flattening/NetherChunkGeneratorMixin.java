package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.NetherCaveCarver;
import net.minecraft.world.gen.chunk.NetherChunkGenerator;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NetherChunkGenerator.class)
class NetherChunkGeneratorMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/class_344;"
            )
    )
    private NetherCaveCarver stationapi_setWorldForCaveGen(Operation<NetherCaveCarver> original, World world, long l) {
        final NetherCaveCarver caveGen = original.call();
        ((CaveGenBaseImpl) caveGen).stationapi_setWorld(world);
        return caveGen;
    }

    @Redirect(
            method = "getChunk",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/world/World;[BII)Lnet/minecraft/world/chunk/Chunk;"
            )
    )
    private Chunk stationapi_redirectChunk(World world, byte[] tiles, int xPos, int zPos) {
        return new FlattenedChunk(world, xPos, zPos);
    }

    @Inject(
            method = "getChunk",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_populateChunk(int j, int par2, CallbackInfoReturnable<Chunk> cir, byte[] tiles) {
        if (cir.getReturnValue() instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles);
    }
}
