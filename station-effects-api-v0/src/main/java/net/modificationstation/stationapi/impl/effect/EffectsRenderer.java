package net.modificationstation.stationapi.impl.effect;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class EffectsRenderer extends DrawContext {
    private final Reference2IntMap<Identifier> effectIcons = new Reference2IntOpenHashMap<>();
    private final List<EntityEffect<?>> renderEffects = new ArrayList<>();
    private TextRenderer textRenderer;
    
    private final Comparator<EntityEffect<?>> comparator = (e1, e2) -> {
        int l1 = getEffectWidth(e1);
        int l2 = getEffectWidth(e2);
        if (l1 == l2) return e1.getType().registryEntry.registryKey().getValue().compareTo(e2.getType().registryEntry.registryKey().getValue());
        return Integer.compare(l2, l1);
    };
    
    public void renderEffects(Minecraft minecraft, float delta, boolean extended) {
        Collection<EntityEffect<?>> effects = minecraft.player.getEffects();
        if (effects.isEmpty()) return;
        
        textRenderer = minecraft.textRenderer;
        
        renderEffects.clear();
        renderEffects.addAll(effects);
        renderEffects.sort(comparator);
        
        int py = 2;
        for (EntityEffect<?> effect : renderEffects) {
            if (extended) {
                String name = effect.getName();
                String desc = effect.getDescription();
                int width = Math.max(
                    textRenderer.getWidth(name),
                    textRenderer.getWidth(desc)
                );
                
                if (effect.isInfinite()) {
                    renderEffectBack(minecraft, py, 30 + width);
                    textRenderer.drawWithShadow(name, 26, py + 5, 0xFFFFFFFF);
                    textRenderer.drawWithShadow(desc, 26, py + 14, 0xFFFFFFFF);
                }
                else {
                    String time = getEffectTime(effect, delta);
                    int timeWidth = textRenderer.getWidth(time);
                    renderEffectBack(minecraft, py, 32 + width + timeWidth);
                    textRenderer.drawWithShadow(time, 26, py + 9, 0xFFFFFFFF);
                    int x = 28 + timeWidth;
                    textRenderer.drawWithShadow(name, x, py + 5, 0xFFFFFFFF);
                    textRenderer.drawWithShadow(desc, x, py + 14, 0xFFFFFFFF);
                }
            }
            else {
                if (effect.isInfinite()) {
                    renderEffectBack(minecraft, py, 26);
                }
                else {
                    String time = getEffectTime(effect, delta);
                    renderEffectBack(minecraft, py, 30 + textRenderer.getWidth(time));
                    textRenderer.drawWithShadow(time, 26, py + 9, 0xFFFFFFFF);
                }
            }
            
            Identifier id = effect.getType().registryEntry.registryKey().getValue();
            int texture = effectIcons.computeIfAbsent(id, k ->
                minecraft.textureManager.getTextureId("/assets/" + id.namespace + "/stationapi/textures/gui/effect/" + id.path + ".png")
            );
            
            renderEffectIcon(py + 5, texture);
            py += 28;
        }
    }
    
    private String getEffectTime(EntityEffect<?> effect, float delta) {
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
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/achievement/bg.png"));
        
        int x = width - 12;
        int y2 = y + 13;
        
        drawTexture(2, y, 96, 202, 14, 14);
        drawTexture(2, y2, 96, 233 - 13, 14, 14);
        
        if (width > 26) {
            width -= 26;
            drawTexture(15, y, 109, 202, width + 1, 14);
            drawTexture(15, y + 13, 109, 220, width + 1, 14);
        }
        
        drawTexture(x, y, 242, 202, 14, 14);
        drawTexture(x, y2, 242, 220, 14, 14);
    }
    
    private int getEffectWidth(EntityEffect<?> effect) {
        if (effect.isInfinite()) return 26;
        String name = effect.getName();
        String desc = effect.getDescription();
        return Math.max(
            textRenderer.getWidth(name),
            textRenderer.getWidth(desc)
        );
    }
}
