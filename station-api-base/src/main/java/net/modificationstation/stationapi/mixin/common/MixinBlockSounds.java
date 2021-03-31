package net.modificationstation.stationapi.mixin.common;

import net.minecraft.block.BlockSounds;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockSounds.class)
public class MixinBlockSounds {

    @Shadow @Final public String sound;

    @Inject(method = {"getWalkSound()Ljava/lang/String;", "getBreakSound()Ljava/lang/String;"}, at = @At(value = "HEAD"), cancellable = true)
    private void hijackStepSound(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(sound.contains(":") ? String.join(":step.", sound.split(":")) : "step." + sound);
    }
}
