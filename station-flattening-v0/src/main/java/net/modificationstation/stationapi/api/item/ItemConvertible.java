package net.modificationstation.stationapi.api.item;

import net.minecraft.item.Item;

/**
 * Represents an object that has an item form.
 */
public interface ItemConvertible {

    /**
     * Gets this object in its item form.
     */
    Item asItem();
}

