package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.class_153;
import net.minecraft.class_43;
import net.minecraft.class_538;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_538.class)
class OverworldChunkGeneratorMixin {
    @Shadow private World field_2260;
    @Shadow private double[] field_2261;
    
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
                    target = "(Lnet/minecraft/world/World;[BII)Lnet/minecraft/class_43;"
            )
    )
    private class_43 stationapi_redirectChunk(World world, byte[] tiles, int xPos, int zPos) {
        return new FlattenedChunk(world, xPos, zPos);
    }

    @Inject(
            method = "method_1806",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_43;method_873()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_populateChunk(int j, int par2, CallbackInfoReturnable<class_43> cir, byte[] tiles, class_43 chunk) {
        if (chunk instanceof FlattenedChunk stationChunk) stationChunk.fromLegacy(tiles);
    }
    
    @ModifyConstant(
            method = "method_1806",
            constant = @Constant(intValue = 32768)
    )
    private int stationapi_changeArrayCapacity(int original) {
        return field_2260.getHeight() << 8;
    }
    
    @Inject(
            method = "method_1798",
            at = @At("HEAD")
    )
    private void stationapi_initChunkLocals(
        int j, int bs, byte[] args, class_153[] ds, double[] par5, CallbackInfo ci,
        @Share("offsetX") LocalIntRef offsetX, @Share("offsetZ") LocalIntRef offsetZ, @Share("vertical") LocalIntRef vertical
    ) {
        int dz = MathHelper.ceilLog2(field_2260.getHeight());
        offsetZ.set(dz);
        offsetX.set(dz + 4);
        vertical.set(field_2260.getHeight() >> 3);
        int length = ((field_2260.getHeight() >> 3) + 1) * 25;
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
    private void stationapi_fixNoiseValues(int j, int bs, byte[] args, class_153[] ds, double[] par5, CallbackInfo ci) {
        int height = (field_2260.getHeight() >> 3) + 1;
        int length = height * 25;
        int bottom = field_2260.getBottomY() >> 3;
        for (int i = length - 1; i >= 0; i--) {
            int y = (i % height) + bottom;
            y = MathHelper.clamp(y, 0, 16);
            int zx = i / height;
            int index = zx * 17 + y;
            field_2261[i] = field_2261[index];
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
    private int stationapi_changeVerticalArraySize(int original) {
        return (field_2260.getHeight() >> 3) + 1;
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
            constant = @Constant(
                    intValue = 0,
                    ordinal = 0
            )
    )
    private int stationapi_changeNoiseBottomY(int original) {
        return field_2260.getBottomY();
    }
    
    @ModifyConstant(
            method = {
                    "method_1798",
                    "method_1797"
            },
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeFillStep(int original) {
        return field_2260.getHeight();
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
