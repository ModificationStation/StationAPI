package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CropBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCrops extends CropBlock implements BlockTemplate {

    public TemplateCrops(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCrops(int i, int j) {
        super(i, j);
    }
}
