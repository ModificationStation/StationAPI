package net.modificationstation.stationloader.impl.common.preset.item;

import net.minecraft.item.PlaceableTileEntity;

public class PlaceableTileEntityWithMeta extends PlaceableTileEntity implements net.modificationstation.stationloader.api.common.preset.item.PlaceableTileEntityWithMeta {

    public PlaceableTileEntityWithMeta(int i) {
        super(i);
        setHasSubItems(true);
    }

    @Override
    public int getMetaData(int i) {
        return i;
    }
}
