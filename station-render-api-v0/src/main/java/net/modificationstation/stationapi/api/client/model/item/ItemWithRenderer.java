package net.modificationstation.stationapi.api.client.model.item;

import net.minecraft.item.ItemInstance;

public interface ItemWithRenderer {

    void render(ItemInstance itemInstance);
}
