package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;

@Deprecated
public interface ItemWithRenderer {

    @Deprecated
    void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, int itemId, int damage, int textureIndex, int x, int y);

    @Deprecated
    default void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, ItemInstance itemInstance, int x, int y) {
        renderItemOnGui(itemRenderer, textRenderer, textureManager, itemInstance.itemId, itemInstance.getDamage(), itemInstance.getTexturePosition(), x, y);
    }
}
