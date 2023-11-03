package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.StoneBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStone extends StoneBlock implements BlockTemplate {
    
    public TemplateStone(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStone(int i, int j) {
        super(i, j);
    }
}
