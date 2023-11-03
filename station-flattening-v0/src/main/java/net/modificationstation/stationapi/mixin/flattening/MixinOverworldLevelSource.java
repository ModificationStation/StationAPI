package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.class_153;
import net.minecraft.class_43;
import net.minecraft.class_538;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_538.class)
public class MixinOverworldLevelSource {
    @Shadow private World level;
    @Shadow private double[] noises;
    
    @ModifyConstant(method = "decorate(Lnet/minecraft/level/source/LevelSource;II)V", constant = @Constant(intValue = 128))
    private int changeMaxHeight(int value) {
        return level.getTopY();
    }

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
    
    @ModifyConstant(method = "getChunk", constant = @Constant(intValue = 32768))
    private int changeArrayCapacity(int original) {
        return level.getHeight() << 8;
    }
    
    @Inject(method = "shapeChunk", at = @At("HEAD"))
    private void initChunkLocals(
        int j, int bs, byte[] args, class_153[] ds, double[] par5, CallbackInfo ci,
        @Share("offsetX") LocalIntRef offsetX, @Share("offsetZ") LocalIntRef offsetZ, @Share("vertical") LocalIntRef vertical
    ) {
        int dz = MathHelper.ceilLog2(level.getHeight());
        offsetZ.set(dz);
        offsetX.set(dz + 4);
        vertical.set(level.getHeight() >> 3);
        int length = ((level.getHeight() >> 3) + 1) * 25;
        if (noises == null || noises.length < length) {
            noises = new double[length];
        }
    }
    
    @Inject(method = "shapeChunk", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/level/source/OverworldLevelSource;calculateNoise([DIIIIII)[D",
        shift = Shift.AFTER
    ))
    private void fixNoiseValues(int j, int bs, byte[] args, class_153[] ds, double[] par5, CallbackInfo ci) {
        int height = (level.getHeight() >> 3) + 1;
        int length = height * 25;
        int bottom = level.getBottomY() >> 3;
        for (int i = length - 1; i >= 0; i--) {
            int y = (i % height) + bottom;
            y = MathHelper.clamp(y, 0, 16);
            int zx = i / height;
            int index = zx * 17 + y;
            noises[i] = noises[index];
        }
    }
    
    @ModifyArg(method = "shapeChunk", index = 5, at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/level/source/OverworldLevelSource;calculateNoise([DIIIIII)[D"
    ))
    private int preventArgChanges(int original) {
        return 17;
    }
    
    @ModifyConstant(method = "shapeChunk", constant = @Constant(intValue = 17))
    private int changeVerticalArraySize(int original) {
        return (level.getHeight() >> 3) + 1;
    }
    
    @ModifyConstant(method = "shapeChunk", constant = @Constant(intValue = 16, ordinal = 0))
    private int changeVerticalIterations(int original, @Share("vertical") LocalIntRef vertical) {
        return vertical.get();
    }
    
    @ModifyConstant(method = "shapeChunk", constant = @Constant(intValue = 0, ordinal = 0))
    private int changeNoiseBottomY(int original) {
        return level.getBottomY();
    }
    
    @ModifyConstant(method = {"shapeChunk", "buildSurface"}, constant = @Constant(intValue = 128))
    private int changeFillStep(int original) {
        return level.getHeight();
    }
    
    @ModifyConstant(method = "shapeChunk", constant = @Constant(intValue = 7))
    private int changeZOffset(int original, @Share("offsetZ") LocalIntRef offsetZ) {
        return offsetZ.get();
    }
    
    @ModifyConstant(method = "shapeChunk", constant = @Constant(intValue = 11))
    private int changeXOffset(int original, @Share("offsetX") LocalIntRef offsetX) {
        return offsetX.get();
    }
    
    @ModifyConstant(method = "shapeChunk", constant = @Constant(intValue = 64))
    private int changeOceanHeight(int original) {
        return 64 - level.getBottomY();
    }
}
