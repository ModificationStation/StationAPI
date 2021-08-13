package net.modificationstation.stationapi.api.multiblock;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;

public abstract class MultiblockController extends BlockBase {

    protected MultiblockController(int id, int tex, Material material) {
        super(id, tex, material);
    }

    abstract MultiblockStructure getStructure();
}
