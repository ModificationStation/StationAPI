package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Obsidian;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateObsidian extends Obsidian implements BlockTemplate {
    
    public TemplateObsidian(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateObsidian(int i, int j) {
        super(i, j);
    }
}
