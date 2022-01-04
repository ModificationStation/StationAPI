package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.util.ScreenScaler;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Mixin(InGame.class)
public class MixinInGame extends DrawableHelper {

    @Inject(
            method = "renderHud(FZII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 5,
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void showCurrentRenderer(float flag, boolean i, int j, int par4, CallbackInfo ci, ScreenScaler var5, int var6, int var7, TextRenderer textRenderer) {
        drawTextWithShadow(textRenderer, "[" + MODID.getName() + "] Current render plugin: " + RenderPlugin.PLUGIN, 2, 96, 14737632);
    }
}
