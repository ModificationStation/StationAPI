package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.BlockItemProvider;
import net.modificationstation.stationapi.api.common.factory.GeneralFactory;
import net.modificationstation.stationapi.api.common.preset.item.PlaceableTileEntityWithMeta;

public interface HasPlaceableTileEntityWithMeta extends BlockItemProvider {

    @Override
    default PlaceableTileEntity getBlockItem(int shiftedID) {
        return (PlaceableTileEntity) GeneralFactory.INSTANCE.newInst(PlaceableTileEntityWithMeta.class, shiftedID);
    }
}
