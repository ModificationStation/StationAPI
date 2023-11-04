package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSand extends SandBlock implements BlockTemplate {
    
    public TemplateSand(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSand(int i, int j) {
        super(i, j);
    }
}
