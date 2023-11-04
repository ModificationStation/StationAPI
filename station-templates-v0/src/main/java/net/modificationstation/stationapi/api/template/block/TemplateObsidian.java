package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.ObsidianBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateObsidian extends ObsidianBlock implements BlockTemplate {
    
    public TemplateObsidian(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateObsidian(int i, int j) {
        super(i, j);
    }
}
