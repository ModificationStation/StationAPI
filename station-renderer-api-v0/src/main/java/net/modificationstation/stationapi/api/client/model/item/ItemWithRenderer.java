package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public interface ItemWithRenderer {

    void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, int itemId, int damage, int textureIndex, int x, int y);

    default void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, ItemStack stack, int x, int y) {
        renderItemOnGui(itemRenderer, textRenderer, textureManager, stack.itemId, stack.getDamage(), stack.getTextureId(), x, y);
    }

    default void renderItemInWorld(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, ItemStack stack, float brightness) {
        GL11.glColor3f(brightness, brightness, brightness);
        renderItemOnGui(itemRenderer, textRenderer, textureManager, stack, 0, 0);
    }

    default void renderItemInHand(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, ItemStack stack, float brightness) {
        GL11.glColor3f(brightness, brightness, brightness);
        renderItemOnGui(itemRenderer, textRenderer, textureManager, stack, 0, 0);
    }
}
