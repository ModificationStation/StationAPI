package net.modificationstation.stationapi.impl.effect;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

public class EffectsRenderer {
	private final Reference2IntMap<Identifier> effectIcons = new Reference2IntOpenHashMap<>();
	
	public void renderEffects(Minecraft minecraft, float delta, boolean extended) {
		Collection<EntityEffect<? extends Entity>> effects = minecraft.player.getRenderEffects();
		if (effects == null || effects.isEmpty()) return;
		
		int py = 2;
		for (EntityEffect<? extends Entity> effect : effects) {
			if (extended) {
				String name = effect.getName();
				String desc = effect.getDescription();
				int width = Math.max(
					minecraft.textRenderer.getWidth(name),
					minecraft.textRenderer.getWidth(desc)
				);
				
				if (effect.isInfinity()) {
					renderEffectBack(minecraft, py, 30 + width);
					minecraft.textRenderer.drawWithShadow(name, 26, py + 5, 0xFFFFFFFF);
					minecraft.textRenderer.drawWithShadow(desc, 26, py + 14, 0xFFFFFFFF);
				}
				else {
					String time = getEffectTime(effect, delta);
					int timeWidth = minecraft.textRenderer.getWidth(time);
					renderEffectBack(minecraft, py, 32 + width + timeWidth);
					minecraft.textRenderer.drawWithShadow(time, 26, py + 9, 0xFFFFFFFF);
					int x = 28 + timeWidth;
					minecraft.textRenderer.drawWithShadow(name, x, py + 5, 0xFFFFFFFF);
					minecraft.textRenderer.drawWithShadow(desc, x, py + 14, 0xFFFFFFFF);
				}
			}
			else {
				if (effect.isInfinity()) {
					renderEffectBack(minecraft, py, 26);
				}
				else {
					String time = getEffectTime(effect, delta);
					renderEffectBack(minecraft, py, 28 + minecraft.textRenderer.getWidth(time));
					minecraft.textRenderer.drawWithShadow(time, 26, py + 9, 0xFFFFFFFF);
				}
			}
			
			Identifier id = effect.getEffectID();
			int texture = effectIcons.computeIfAbsent(id, k ->
				minecraft.textureManager.getTextureId("/assets/" + id.namespace + "/stationapi/textures/gui/effect/" + id.path + ".png")
			);
			
			renderEffectIcon(py + 5, texture);
			py += 28;
		}
	}
	
	private String getEffectTime(EntityEffect<? extends Entity> effect, float delta) {
		float ticks = effect.getTicks() + (1.0F - delta);
		int seconds = Math.round(ticks / 20.0F);
		int minutes = seconds / 60;
		return String.format("%02d:%02d", minutes, seconds);
	}
	
	private void renderEffectIcon(int y, int texture) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.startQuads();
		
		int y2 = y + 16;
		
		tessellator.vertex(7, y2, 0.0F, 0.0F, 1.0F);
		tessellator.vertex(23, y2, 0.0F, 1.0F, 1.0F);
		tessellator.vertex(23, y, 0.0F, 1.0F, 0.0F);
		tessellator.vertex(7, y, 0.0F, 0.0F, 0.0F);
		
		tessellator.draw();
	}
	
	private void renderEffectBack(Minecraft minecraft, int y, int width) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/assets/stationapi/textures/gui/effect_back.png"));
		
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.startQuads();
		
		int y2 = y + 26;
		
		tessellator.vertex(2, y2, 0.0F, 0.0F, 1.0F);
		tessellator.vertex(14, y2, 0.0F, 0.5F, 1.0F);
		tessellator.vertex(14, y, 0.0F, 0.5F, 0.0F);
		tessellator.vertex(2, y, 0.0F, 0.0F, 0.0F);
		
		int x2 = width + 2;
		int x1 = x2 - 14;
		
		if (width > 26) {
			tessellator.vertex(14, y2, 0.0F, 0.5F, 1.0F);
			tessellator.vertex(x1, y2, 0.0F, 0.5F, 1.0F);
			tessellator.vertex(x1, y, 0.0F, 0.5F, 0.0F);
			tessellator.vertex(14, y, 0.0F, 0.5F, 0.0F);
		}
		
		tessellator.vertex(x1, y2, 0.0F, 0.5F, 1.0F);
		tessellator.vertex(x2, y2, 0.0F, 1.0F, 1.0F);
		tessellator.vertex(x2, y, 0.0F, 1.0F, 0.0F);
		tessellator.vertex(x1, y, 0.0F, 0.5F, 0.0F);
		
		tessellator.draw();
	}
}
