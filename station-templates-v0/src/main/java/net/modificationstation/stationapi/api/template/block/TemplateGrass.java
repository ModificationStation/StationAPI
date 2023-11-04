package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GrassBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGrass extends GrassBlock implements BlockTemplate {

    public TemplateGrass(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateGrass(int id) {
        super(id);
    }
}
