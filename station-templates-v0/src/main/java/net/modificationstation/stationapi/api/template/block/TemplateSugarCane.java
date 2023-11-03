package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SugarCaneBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSugarCane extends SugarCaneBlock implements BlockTemplate {
    
    public TemplateSugarCane(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSugarCane(int i, int j) {
        super(i, j);
    }
}
