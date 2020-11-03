package net.modificationstation.stationloader.api.client.item;

import net.minecraft.item.ItemInstance;

public interface ArmorTextureProvider {

    String getChestplateModelTexture(ItemInstance itemInstance);

    String getOtherModelTexture(ItemInstance itemInstance);
}
