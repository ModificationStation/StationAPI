package net.modificationstation.stationapi.api.client.item;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;

public interface CustomItemOverlay {

    void renderItemOverlay(int itemX, int itemY, ItemInstance itemInstance, TextRenderer textRenderer, TextureManager textureManager);
}
