package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_598;
import net.minecraft.class_84;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.FloatBuffer;

@Mixin(class_598.class)
public abstract class Mixinclass_598 extends class_84 {

    @Redirect(
            method = "method_1975()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glGetFloat(ILjava/nio/FloatBuffer;)V",
                    ordinal = 0,
                    remap = false
            )
    )
    private void redirectProjectionMatrix(int pname, FloatBuffer params) {
        RenderSystem.getProjectionMatrix().writeToBuffer(params);
    }

    @Redirect(
            method = "method_1975()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glGetFloat(ILjava/nio/FloatBuffer;)V",
                    ordinal = 1,
                    remap = false
            )
    )
    private void redirectModelViewMatrix(int pname, FloatBuffer params) {
        RenderSystem.getModelViewMatrix().writeToBuffer(params);
    }
}
