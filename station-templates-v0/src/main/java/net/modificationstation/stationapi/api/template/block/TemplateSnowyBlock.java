package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SnowyBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSnowyBlock extends SnowyBlock implements BlockTemplate {
    public TemplateSnowyBlock(Identifier identifier, int texUVStart) {
        this(BlockTemplate.getNextId(), texUVStart);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSnowyBlock(int id, int texUVStart) {
        super(id, texUVStart);
    }
}
