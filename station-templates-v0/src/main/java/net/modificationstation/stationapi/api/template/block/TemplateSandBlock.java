package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSandBlock extends SandBlock implements BlockTemplate {
    public TemplateSandBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSandBlock(int i, int j) {
        super(i, j);
    }
}
