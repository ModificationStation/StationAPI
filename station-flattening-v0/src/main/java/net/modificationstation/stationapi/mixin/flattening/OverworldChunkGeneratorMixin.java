package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.class_415;
import net.minecraft.class_538;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
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

@Mixin(class_538.class)
class OverworldChunkGeneratorMixin {
    @Shadow private World field_2260;
    @Shadow private double[] field_2261;
    @Unique private double[] densityCache;

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/class_415;"
            )
    )
    private class_415 stationapi_setWorldForCaveGen(Operation<class_415> original, World world, long l) {
        final class_415 caveGen = original.call();
        ((CaveGenBaseImpl) caveGen).stationapi_setWorld(world);
        return caveGen;
    }
    
    @ModifyConstant(
            method = "method_1803",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeMaxHeight(int value) {
        return field_2260.getTopY();
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
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;method_873()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_populateChunk(int j, int par2, CallbackInfoReturnable<Chunk> cir, byte[] tiles, Chunk chunk) {
        if (chunk instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles);
    }
    
    @ModifyConstant(
            method = "method_1806",
            constant = @Constant(intValue = 32768)
    )
    private int stationapi_changeArrayCapacity(int original) {
        return 1 << (MathHelper.ceilLog2(field_2260.getHeight()) + 8);
    }
    
    @Inject(
            method = "method_1798",
            at = @At("HEAD")
    )
    private void stationapi_initChunkLocals(
        int j, int bs, byte[] args, Biome[] ds, double[] par5, CallbackInfo ci,
        @Share("offsetX") LocalIntRef offsetX, @Share("offsetZ") LocalIntRef offsetZ, @Share("vertical") LocalIntRef vertical, @Share("height") LocalIntRef height
    ) {
        int dz = MathHelper.ceilLog2(field_2260.getHeight());
        height.set(1 << dz);
        offsetZ.set(dz);
        offsetX.set(dz + 4);
        int heightPacked = 1 << (dz - 3);
        vertical.set(heightPacked);
        int length = (heightPacked + 1) * 25;
        if (field_2261 == null || field_2261.length < length) {
            field_2261 = new double[length];
        }
    }
    
    @Inject(
            method = "method_1798",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_538;method_1799([DIIIIII)[D",
                    shift = Shift.AFTER
            )
    )
    private void stationapi_fixNoiseValues(int j, int bs, byte[] args, Biome[] ds, double[] par5, CallbackInfo ci, @Share("vertical") LocalIntRef vertical) {
        int height = vertical.get() + 1;
        int length = height * 25;
        int bottom = field_2260.getBottomY() >> 3;
        
        if (densityCache == null || densityCache.length != length) {
            densityCache = new double[field_2261.length];
        }
        System.arraycopy(field_2261, 0, densityCache, 0, field_2261.length);
        
        for (int i = 0; i < length; i++) {
            int y = (i % height) + bottom;
            y = MathHelper.clamp(y, 0, 16);
            int zx = i / height;
            int index = zx * 17 + y;
            field_2261[i] = densityCache[index];
        }
    }
    
    @ModifyArg(
            method = "method_1798",
            index = 5,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_538;method_1799([DIIIIII)[D"
            )
    )
    private int stationapi_preventArgChanges(int original) {
        return 17;
    }
    
    @ModifyConstant(
            method = "method_1798",
            constant = @Constant(intValue = 17)
    )
    private int stationapi_changeVerticalArraySize(int original, @Share("vertical") LocalIntRef vertical) {
        return vertical.get() + 1;
    }
    
    @ModifyConstant(
            method = "method_1798",
            constant = @Constant(
                    intValue = 16,
                    ordinal = 0
            )
    )
    private int stationapi_changeVerticalIterations(int original, @Share("vertical") LocalIntRef vertical) {
        return vertical.get();
    }
    
    @ModifyConstant(
            method = "method_1798",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeFillStep(int original, @Share("height") LocalIntRef height) {
        return height.get();
    }
    
    @Inject(method = "method_1797", at = @At("HEAD"))
    private void stationapi_initLocals(int j, int bs, byte[] args, Biome[] par4, CallbackInfo ci, @Share("vertical2") LocalIntRef vertical2) {
        vertical2.set(1 << MathHelper.ceilLog2(field_2260.getHeight()));
    }
    
    @ModifyConstant(
        method = "method_1797",
        constant = @Constant(intValue = 128)
    )
    private int stationapi_changeFillStep2(int original, @Share("vertical2") LocalIntRef vertical2) {
        return vertical2.get();
    }
    
    @ModifyConstant(
            method = "method_1798",
            constant = @Constant(intValue = 7)
    )
    private int stationapi_changeZOffset(int original, @Share("offsetZ") LocalIntRef offsetZ) {
        return offsetZ.get();
    }
    
    @ModifyConstant(
            method = "method_1798",
            constant = @Constant(intValue = 11)
    )
    private int stationapi_changeXOffset(int original, @Share("offsetX") LocalIntRef offsetX) {
        return offsetX.get();
    }
    
    @ModifyConstant(
            method = "method_1798",
            constant = @Constant(intValue = 64)
    )
    private int stationapi_changeOceanHeight(int original) {
        return 64 - field_2260.getBottomY();
    }
}
