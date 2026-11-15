package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.chunk.SkyChunkGenerator;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SkyChunkGenerator.class)
class SkylandsChunkGeneratorMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/world/gen/carver/CaveCarver;"
            )
    )
    private CaveCarver stationapi_setWorldForCaveGen(Operation<CaveCarver> original, World world, long l) {
        final CaveCarver caveGen = original.call();
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

    @WrapOperation(
            method = "getChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;populateHeightMap()V"
            )
    )
    private void stationapi_populateChunk(Chunk chunk, Operation<Void> original, @Local byte[] tiles) {
        // the sections, height map and sky light in one go from the generator's array
        if (chunk instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles, true);
        else original.call(chunk);
    }
}
