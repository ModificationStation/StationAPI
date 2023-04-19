package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Log;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateLog extends Log implements BlockTemplate {
    
    public TemplateLog(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateLog(int i) {
        super(i);
    }
}
