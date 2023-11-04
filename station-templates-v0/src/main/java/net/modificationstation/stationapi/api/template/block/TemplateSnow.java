package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SnowyBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSnow extends SnowyBlock implements BlockTemplate {
    
    public TemplateSnow(Identifier identifier, int texUVStart) {
        this(BlockTemplate.getNextId(), texUVStart);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSnow(int id, int texUVStart) {
        super(id, texUVStart);
    }
}
