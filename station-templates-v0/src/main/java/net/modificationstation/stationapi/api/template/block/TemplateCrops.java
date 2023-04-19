package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Crops;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCrops extends Crops implements BlockTemplate {

    public TemplateCrops(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCrops(int i, int j) {
        super(i, j);
    }
}
