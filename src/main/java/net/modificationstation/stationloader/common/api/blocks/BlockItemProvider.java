package net.modificationstation.stationloader.common.api.blocks;

import net.minecraft.item.PlaceableBlock;

public interface BlockItemProvider {

    PlaceableBlock getBlockItem(int shiftedID);
}
