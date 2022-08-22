package net.modificationstation.stationapi.api.block;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItemsBlock extends ItemConvertible {

    @Override
    default ItemBase asItem() {
        return Util.assertImpl();
    }
}
