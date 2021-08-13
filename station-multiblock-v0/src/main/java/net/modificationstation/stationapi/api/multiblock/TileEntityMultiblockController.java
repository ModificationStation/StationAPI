package net.modificationstation.stationapi.api.multiblock;

import net.minecraft.inventory.InventoryBase;
import net.minecraft.tileentity.TileEntityBase;

public abstract class TileEntityMultiblockController extends TileEntityBase implements InventoryBase {

    abstract void registerStructureListeners();

}
