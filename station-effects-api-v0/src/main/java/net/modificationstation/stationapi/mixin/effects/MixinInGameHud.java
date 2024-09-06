package net.modificationstation.stationapi.mixin.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.InGameHud;
import net.modificationstation.stationapi.impl.effect.EffectsRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
	@Unique private final EffectsRenderer stationapi_effectRenderer = new EffectsRenderer();
	
	@Shadow private Minecraft minecraft;
	
	@Inject(method = "render", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V",
		shift = Shift.AFTER,
		ordinal = 0
	))
	private void stationapi_renderEffects(float delta, boolean i, int j, int par4, CallbackInfo ci) {
		if (minecraft.options.debugHud) return;
		stationapi_effectRenderer.renderEffects(minecraft, 2, delta, false);
	}
}
