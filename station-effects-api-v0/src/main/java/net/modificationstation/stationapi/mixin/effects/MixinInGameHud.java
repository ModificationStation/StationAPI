package net.modificationstation.stationapi.mixin.effects;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(InGameHud.class)
public class MixinInGameHud extends DrawContext {
	@Unique private final Reference2IntMap<Identifier> stationapi_effectIcons = new Reference2IntOpenHashMap<>();
	
	@Shadow private Minecraft minecraft;
	
	@Inject(method = "render", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V",
		shift = Shift.AFTER,
		ordinal = 0
	))
	private void stationapi_renderEffects(float delta, boolean i, int j, int par4, CallbackInfo ci) {
		if (minecraft.options.debugHud) return;
		Collection<EntityEffect<? extends Entity>> effects = minecraft.player.getRenderEffects();
		if (effects == null || effects.isEmpty()) return;
		
		int py = 2;
		for (EntityEffect<? extends Entity> effect : effects) {
			float ticks = effect.getTicks() + (1.0F - delta);
			int seconds = Math.round(ticks / 20.0F);
			int minutes = seconds / 60;
			String time = String.format("%02d:%02d", minutes, seconds);
			stationapi_renderEffectBack(py, 26 + minecraft.textRenderer.getWidth(time));
			minecraft.textRenderer.drawWithShadow(time, 24, py + 8, 0xFFFFFFFF);
			Identifier id = effect.getEffectID();
			int texture = stationapi_effectIcons.computeIfAbsent(id, k ->
				minecraft.textureManager.getTextureId("/assets/" + id.namespace + "/stationapi/textures/gui/effect/" + id.path + ".png")
			);
			stationapi_renderEffectIcon(py + 4, texture);
			py += 26;
		}
	}
	
	@Unique
	private void stationapi_renderEffectIcon(int y, int texture) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.startQuads();
		
		int y2 = y + 16;
		
		tessellator.vertex(6, y2, 0.0F, 0.0F, 1.0F);
		tessellator.vertex(22, y2, 0.0F, 1.0F, 1.0F);
		tessellator.vertex(22, y, 0.0F, 1.0F, 0.0F);
		tessellator.vertex(6, y, 0.0F, 0.0F, 0.0F);
		
		tessellator.draw();
	}
	
	@Unique
	private void stationapi_renderEffectBack(int y, int width) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/assets/stationapi/textures/gui/effect_back.png"));
		
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.startQuads();
		
		int y2 = y + 24;
		
		tessellator.vertex(2, y2, 0.0F, 0.0F, 1.0F);
		tessellator.vertex(14, y2, 0.0F, 0.5F, 1.0F);
		tessellator.vertex(14, y, 0.0F, 0.5F, 0.0F);
		tessellator.vertex(2, y, 0.0F, 0.0F, 0.0F);
		
		int x2 = width + 2;
		int x1 = x2 - 12;
		
		tessellator.vertex(14, y2, 0.0F, 0.5F, 1.0F);
		tessellator.vertex(x1, y2, 0.0F, 0.5F, 1.0F);
		tessellator.vertex(x1, y, 0.0F, 0.5F, 0.0F);
		tessellator.vertex(14, y, 0.0F, 0.5F, 0.0F);
		
		tessellator.vertex(x1, y2, 0.0F, 0.5F, 1.0F);
		tessellator.vertex(x2, y2, 0.0F, 1.0F, 1.0F);
		tessellator.vertex(x2, y, 0.0F, 1.0F, 0.0F);
		tessellator.vertex(x1, y, 0.0F, 0.5F, 0.0F);
		
		tessellator.draw();
	}
}
