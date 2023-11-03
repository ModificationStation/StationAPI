package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;

@Deprecated
public interface ItemWithRenderer {

    @Deprecated
    void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, int itemId, int damage, int textureIndex, int x, int y);

    @Deprecated
    default void renderItemOnGui(ItemRenderer itemRenderer, TextRenderer textRenderer, TextureManager textureManager, ItemStack itemInstance, int x, int y) {
        renderItemOnGui(itemRenderer, textRenderer, textureManager, itemInstance.itemId, itemInstance.getDamage(), itemInstance.method_725(), x, y);
    }
}
