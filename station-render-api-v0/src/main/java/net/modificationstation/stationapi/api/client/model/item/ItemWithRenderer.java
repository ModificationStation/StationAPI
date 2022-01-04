package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;

public interface ItemWithRenderer {

    void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, int itemId, int damage, int textureIndex, int textureX, int textureY);

    default void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, ItemInstance itemInstance, int textureX, int textureY) {
        renderItemOnGui(textRenderer, textureManager, itemInstance.itemId, itemInstance.getDamage(), itemInstance.getTexturePosition(), textureX, textureY);
    }
}
