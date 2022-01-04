package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.impl.client.model.BakedModelRendererImpl;

public interface ItemModelProvider extends ItemWithRenderer {

    BakedModelRenderer RENDERER = new BakedModelRendererImpl(null, null);

    default Model getModel(ItemInstance itemInstance) {
        return getModel(itemInstance.getDamage());
    }

    Model getModel(int damage);

    @Override
    default void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, ItemInstance itemInstance, int textureX, int textureY) {
        RENDERER.renderInventory(getModel(itemInstance).getBaked());
    }

    @Override
    default void renderItemOnGui(TextRenderer textRenderer, TextureManager textureManager, int itemId, int damage, int textureIndex, int textureX, int textureY) {
        RENDERER.renderInventory(getModel(damage).getBaked());
    }
}
