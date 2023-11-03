package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.class_555;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.impl.worldgen.FogRendererImpl;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_555.class)
public class MixinGameRenderer {
    @Shadow private Minecraft minecraft;
    @Shadow float field_2346;
    @Shadow float field_2347;
    @Shadow float field_2348;

    @Inject(
            method = "method_1842(IF)V",
            at = @At("HEAD")
    )
    private void changeFogColor(int i, float delta, CallbackInfo info) {
        FogRendererImpl.setupFog(minecraft, delta);
        field_2346 = FogRendererImpl.getR();
        field_2347 = FogRendererImpl.getG();
        field_2348 = FogRendererImpl.getB();
    }

    @Inject(
            method = "method_1852(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glClearColor(FFFF)V",
                    remap = false,
                    shift = Shift.AFTER
            )
    )
    private void clearWithFogColor(float delta, CallbackInfo info) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(
                FogRendererImpl.getR(),
                FogRendererImpl.getG(),
                FogRendererImpl.getB(),
                1F
        );
    }
}
