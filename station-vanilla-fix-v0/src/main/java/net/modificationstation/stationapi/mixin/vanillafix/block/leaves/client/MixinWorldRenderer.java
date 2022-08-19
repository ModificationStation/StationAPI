package net.modificationstation.stationapi.mixin.vanillafix.block.leaves.client;

import net.minecraft.block.Leaves;
import net.minecraft.client.render.WorldRenderer;
import net.modificationstation.stationapi.api.vanillafix.block.FixedLeavesBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Redirect(
            method = "method_1537()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Leaves;method_991(Z)V"
            )
    )
    private void redirectGraphicsSetting(Leaves instance, boolean b) {
        FixedLeavesBase.isTransparent = b;
    }
}
