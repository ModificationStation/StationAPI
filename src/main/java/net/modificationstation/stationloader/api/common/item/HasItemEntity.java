package net.modificationstation.stationloader.api.common.item;

import net.minecraft.item.ItemInstance;

public interface HasItemEntity {

    ItemEntity getItemEntity();

    void setItemEntity(ItemEntity itemEntity);

    static HasItemEntity cast(ItemInstance itemInstance) {
        return HasItemEntity.class.cast(itemInstance);
    }
}
