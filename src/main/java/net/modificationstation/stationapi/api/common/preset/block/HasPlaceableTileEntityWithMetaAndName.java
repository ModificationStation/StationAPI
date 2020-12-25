package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.BlockItemProvider;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.api.common.preset.item.PlaceableTileEntityWithMetaAndName;

public interface HasPlaceableTileEntityWithMetaAndName extends BlockItemProvider {

    @Override
    default PlaceableTileEntity getBlockItem(int shiftedID) {
        return (PlaceableTileEntity) GeneralFactory.INSTANCE.newInst(PlaceableTileEntityWithMetaAndName.class, shiftedID);
    }
}
