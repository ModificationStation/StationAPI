package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.impl.client.model.BakedModelRenderer;

public interface ItemModelProvider extends ItemWithRenderer {

    Model getModel(ItemInstance itemInstance);

    @Override
    default void render(ItemInstance itemInstance) {
        BakedModelRenderer.renderInventory(getModel(itemInstance).getBaked());
    }
}
