package net.modificationstation.stationapi.api.item.nbt;

import net.minecraft.item.ItemInstance;

@Deprecated
public interface HasItemEntity {

    @Deprecated
    static HasItemEntity cast(ItemInstance itemInstance) {
        return HasItemEntity.class.cast(itemInstance);
    }

    @Deprecated
    ItemEntity getItemEntity();

    @Deprecated
    void setItemEntity(ItemEntity itemEntity);
}
