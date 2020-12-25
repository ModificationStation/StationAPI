package net.modificationstation.stationapi.api.client.item;

import net.minecraft.item.ItemInstance;

public interface ArmorTextureProvider {

    String getChestplateModelTexture(ItemInstance itemInstance);

    String getOtherModelTexture(ItemInstance itemInstance);
}
