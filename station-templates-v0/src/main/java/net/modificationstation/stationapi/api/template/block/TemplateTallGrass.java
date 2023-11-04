package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TallPlantBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTallGrass extends TallPlantBlock implements BlockTemplate {
    
    public TemplateTallGrass(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTallGrass(int i, int j) {
        super(i, j);
    }
}
