package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GravelBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateGravel extends GravelBlock implements BlockTemplate {
    
    public TemplateGravel(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateGravel(int i, int j) {
        super(i, j);
    }
}
