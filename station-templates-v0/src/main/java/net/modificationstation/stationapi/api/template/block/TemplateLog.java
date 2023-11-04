package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LogBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLog extends LogBlock implements BlockTemplate {
    
    public TemplateLog(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateLog(int i) {
        super(i);
    }
}
