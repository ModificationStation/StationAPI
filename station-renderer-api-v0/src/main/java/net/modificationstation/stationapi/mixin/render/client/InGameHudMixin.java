package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.class_564;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Mixin(InGameHud.class)
class InGameHudMixin extends DrawContext {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Ljava/lang/String;III)V",
                    ordinal = 5,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_showCurrentRenderer(float flag, boolean i, int j, int par4, CallbackInfo ci, class_564 var5, int var6, int var7, TextRenderer textRenderer) {
        drawTextWithShadow(textRenderer, "[" + NAMESPACE.getName() + "] Active renderer: " + (RendererAccess.INSTANCE.hasRenderer() ? Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).getClass().getSimpleName() : "none (vanilla)"), 2, 98, 14737632);
    }
}
