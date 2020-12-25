package net.modificationstation.stationapi.impl.common.preset.item;

import net.minecraft.item.ItemInstance;

public class PlaceableTileEntityWithMetaAndName extends PlaceableTileEntityWithMeta implements net.modificationstation.stationapi.api.common.preset.item.PlaceableTileEntityWithMetaAndName {

    public PlaceableTileEntityWithMetaAndName(int i) {
        super(i);
    }

    @Override
    public String getTranslationKey(ItemInstance item) {
        return getTranslationKey() + item.getDamage();
    }
}
