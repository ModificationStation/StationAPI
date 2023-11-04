package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SoulSandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSoulSandBlock extends SoulSandBlock implements BlockTemplate {
    public TemplateSoulSandBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSoulSandBlock(int i, int j) {
        super(i, j);
    }
}
