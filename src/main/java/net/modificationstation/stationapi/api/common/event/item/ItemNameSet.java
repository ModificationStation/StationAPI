package net.modificationstation.stationapi.api.common.event.item;

import net.minecraft.item.ItemBase;

public class ItemNameSet extends ItemEvent {

    public String newName;

    public ItemNameSet(ItemBase item, String newName) {
        super(item);
        this.newName = newName;
    }
}
