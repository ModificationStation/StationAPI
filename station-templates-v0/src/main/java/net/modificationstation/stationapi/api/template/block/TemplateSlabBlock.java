package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SlabBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSlabBlock extends SlabBlock implements BlockTemplate {
    public TemplateSlabBlock(Identifier identifier, boolean flag) {
        this(BlockTemplate.getNextId(), flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSlabBlock(int i, boolean flag) {
        super(i, flag);
    }
}
