package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.ObsidianBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateObsidianBlock extends ObsidianBlock implements BlockTemplate {
    public TemplateObsidianBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateObsidianBlock(int i, int j) {
        super(i, j);
    }
}
