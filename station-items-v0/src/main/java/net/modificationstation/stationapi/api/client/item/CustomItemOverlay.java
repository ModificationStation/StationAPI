package net.modificationstation.stationapi.api.client.item;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;

public interface CustomItemOverlay {
    void renderItemOverlay(ItemRenderer itemRenderer, int itemX, int itemY, ItemStack stack, TextRenderer textRenderer, TextureManager textureManager);
}
