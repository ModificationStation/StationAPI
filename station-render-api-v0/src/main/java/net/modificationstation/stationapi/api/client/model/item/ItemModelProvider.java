package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.client.model.Model;

public interface ItemModelProvider extends ItemWithRenderer {

    Model getModel(ItemInstance itemInstance);

    @Override
    default void render(ItemInstance itemInstance) {
//        BakedModelRendererImpl.renderInventory(getModel(itemInstance).getBaked());
    }
}
