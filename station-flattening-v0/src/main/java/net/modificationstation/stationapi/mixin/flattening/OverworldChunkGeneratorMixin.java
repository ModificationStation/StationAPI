package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = OverworldChunkGenerator.class, priority = 4000)
class OverworldChunkGeneratorMixin {
    @Shadow private World world;
    @Shadow private double[] heightMap;
    @Unique private double[] densityCache;

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
    
    @ModifyConstant(
            method = "decorate",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeMaxHeight(int value) {
        return world.getTopY();
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
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;populateHeightMap()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_populateChunk(int j, int par2, CallbackInfoReturnable<Chunk> cir, byte[] tiles, Chunk chunk) {
        if (chunk instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles);
    }
    
    @ModifyConstant(
            method = "getChunk",
            constant = @Constant(intValue = 32768)
    )
    private int stationapi_changeArrayCapacity(int original) {
        return 1 << (MathHelper.ceilLog2(world.getHeight()) + 8);
    }
    
    @Inject(
            method = "buildTerrain",
            at = @At("HEAD")
    )
    private void stationapi_initChunkLocals(
        int j, int bs, byte[] args, Biome[] ds, double[] par5, CallbackInfo ci,
        @Share("offsetX") LocalIntRef offsetX, @Share("offsetZ") LocalIntRef offsetZ, @Share("vertical") LocalIntRef vertical, @Share("height") LocalIntRef height
    ) {
        int dz = MathHelper.ceilLog2(world.getHeight());
        height.set(1 << dz);
        offsetZ.set(dz);
        offsetX.set(dz + 4);
        int heightPacked = 1 << (dz - 3);
        vertical.set(heightPacked);
        int length = (heightPacked + 1) * 25;
        if (heightMap == null || heightMap.length < length) {
            heightMap = new double[length];
        }
    }
    
    @Inject(
            method = "buildTerrain",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/chunk/OverworldChunkGenerator;generateHeightMap([DIIIIII)[D",
                    shift = Shift.AFTER
            )
    )
    private void stationapi_fixNoiseValues(int j, int bs, byte[] args, Biome[] ds, double[] par5, CallbackInfo ci, @Share("vertical") LocalIntRef vertical) {
        int height = vertical.get() + 1;
        int length = height * 25;
        int bottom = world.getBottomY() >> 3;
        
        if (densityCache == null || densityCache.length != length) {
            densityCache = new double[heightMap.length];
        }
        System.arraycopy(heightMap, 0, densityCache, 0, heightMap.length);
        
        for (int i = 0; i < length; i++) {
            int y = (i % height) + bottom;
            y = MathHelper.clamp(y, 0, 16);
            int zx = i / height;
            int index = zx * 17 + y;
            heightMap[i] = densityCache[index];
        }
    }
    
    @ModifyArg(
            method = "buildTerrain",
            index = 5,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/chunk/OverworldChunkGenerator;generateHeightMap([DIIIIII)[D"
            )
    )
    private int stationapi_preventArgChanges(int original) {
        return 17;
    }
    
    @ModifyConstant(
            method = "buildTerrain",
            constant = @Constant(intValue = 17)
    )
    private int stationapi_changeVerticalArraySize(int original, @Share("vertical") LocalIntRef vertical) {
        return vertical.get() + 1;
    }
    
    @ModifyConstant(
            method = "buildTerrain",
            constant = @Constant(
                    intValue = 16,
                    ordinal = 0
            )
    )
    private int stationapi_changeVerticalIterations(int original, @Share("vertical") LocalIntRef vertical) {
        return vertical.get();
    }
    
    @ModifyConstant(
            method = "buildTerrain",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeFillStep(int original, @Share("height") LocalIntRef height) {
        return height.get();
    }
    
    @Inject(method = "buildSurfaces", at = @At("HEAD"))
    private void stationapi_initLocals(int j, int bs, byte[] args, Biome[] par4, CallbackInfo ci, @Share("vertical2") LocalIntRef vertical2) {
        vertical2.set(MathHelper.smallestEncompassingPowerOfTwo(world.getHeight()));
    }

    @ModifyExpressionValue(
            method = "buildSurfaces",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=127"
            )
    )
    private int stationapi_changeTopYM1(int constant) {
        return world.getTopY() - 1;
    }

    @ModifyConstant(
            method = "buildSurfaces",
            constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO)
    )
    private int stationapi_changeBottomY(int constant) {
        return world.getBottomY();
    }
    
    @ModifyConstant(
        method = "buildSurfaces",
        constant = @Constant(intValue = 128)
    )
    private int stationapi_changeFillStep2(int original, @Share("vertical2") LocalIntRef vertical2) {
        return vertical2.get();
    }

    @ModifyVariable(
            method = "buildSurfaces",
            at = @At("STORE"),
            index = 18
    )
    private int stationapi_adjustForDepth(int value) {
        return value - world.getBottomY();
    }
    
    @ModifyConstant(
            method = "buildTerrain",
            constant = @Constant(intValue = 7)
    )
    private int stationapi_changeZOffset(int original, @Share("offsetZ") LocalIntRef offsetZ) {
        return offsetZ.get();
    }
    
    @ModifyConstant(
            method = "buildTerrain",
            constant = @Constant(intValue = 11)
    )
    private int stationapi_changeXOffset(int original, @Share("offsetX") LocalIntRef offsetX) {
        return offsetX.get();
    }
    
    @ModifyConstant(
            method = "buildTerrain",
            constant = @Constant(intValue = 64)
    )
    private int stationapi_changeOceanHeight(int original) {
        return 64 - world.getBottomY();
    }
}
