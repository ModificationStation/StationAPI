package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.entity.PistonExtensionBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateMovingPiston extends PistonExtensionBlock implements BlockTemplate {
    
    public TemplateMovingPiston(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateMovingPiston(int id) {
        super(id);
    }
}
