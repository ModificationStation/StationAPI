package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneOreBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneOre extends RedstoneOreBlock implements BlockTemplate {
    
    public TemplateRedstoneOre(Identifier identifier, int j, boolean isLit) {
        this(BlockTemplate.getNextId(), j, isLit);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstoneOre(int i, int j, boolean isLit) {
        super(i, j, isLit);
    }
}
