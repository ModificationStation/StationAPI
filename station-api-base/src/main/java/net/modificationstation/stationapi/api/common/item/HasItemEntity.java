package net.modificationstation.stationapi.api.common.item;

import net.minecraft.item.ItemInstance;

public interface HasItemEntity {

    static HasItemEntity cast(ItemInstance itemInstance) {
        return HasItemEntity.class.cast(itemInstance);
    }

    ItemEntity getItemEntity();

    void setItemEntity(ItemEntity itemEntity);
}
