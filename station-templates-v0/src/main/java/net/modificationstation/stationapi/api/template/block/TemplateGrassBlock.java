package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GrassBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGrassBlock extends GrassBlock implements BlockTemplate {
    public TemplateGrassBlock(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateGrassBlock(int id) {
        super(id);
    }
}
