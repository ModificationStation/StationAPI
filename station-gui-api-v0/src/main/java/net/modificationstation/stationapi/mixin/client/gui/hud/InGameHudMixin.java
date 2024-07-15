package net.modificationstation.stationapi.mixin.client.gui.hud;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_564;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.GameOptions;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.hud.*;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(InGameHud.class)
abstract class InGameHudMixin extends DrawContext {
    @Shadow private Minecraft minecraft;

    @Redirect(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/option/GameOptions;debugHud:Z",
                    ordinal = 0,
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean stationapi_renderText(GameOptions options, @Local TextRenderer tr, @Local class_564 scaler) {
        GL11.glPushMatrix();
        if (Minecraft.failedSessionCheckTime > 0L) {
            GL11.glTranslatef(0.0F, 32.0F, 0.0F);
        }
        var event = HudTextRenderEvent.builder().debug(options.debugHud).minecraft(minecraft).build();
        StationAPI.EVENT_BUS.post(event);
        var version = event.getVersion();
        if (version != null) tr.drawWithShadow(version.text, 2, 2, version.color);
        for (int i = 0, y = 2; i < event.left.size(); i++) {
            var line = event.left.get(i);
            if (i > 0 || version != null) y += line.offset;
            tr.drawWithShadow(line.text, 2, y, line.color);
        }
        for (int i = 0, y = 2; i < event.right.size(); i++) {
            var line = event.right.get(i);
            if (i > 0) y += line.offset;
            tr.drawWithShadow(line.text, scaler.method_1857() - tr.getWidth(line.text) - 2, y, line.color);
        }
        GL11.glPopMatrix();
        return false;
    }
}
