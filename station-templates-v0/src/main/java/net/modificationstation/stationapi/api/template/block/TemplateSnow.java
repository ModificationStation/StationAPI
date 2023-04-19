package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Snow;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSnow extends Snow implements BlockTemplate {
    
    public TemplateSnow(Identifier identifier, int texUVStart) {
        this(BlockTemplate.getNextId(), texUVStart);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSnow(int id, int texUVStart) {
        super(id, texUVStart);
    }
}
