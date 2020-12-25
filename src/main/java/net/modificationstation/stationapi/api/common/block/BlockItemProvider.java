package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;

public interface BlockItemProvider {

    PlaceableTileEntity getBlockItem(int shiftedID);
}
