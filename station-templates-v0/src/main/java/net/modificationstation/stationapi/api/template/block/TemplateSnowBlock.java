package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SnowBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSnowBlock extends SnowBlock implements BlockTemplate {
    
    public TemplateSnowBlock(Identifier identifier, int texUVStart) {
        this(BlockTemplate.getNextId(), texUVStart);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSnowBlock(int id, int texUVStart) {
        super(id, texUVStart);
    }
}
