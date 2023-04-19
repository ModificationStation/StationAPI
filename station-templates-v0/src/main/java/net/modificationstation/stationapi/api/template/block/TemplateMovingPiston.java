package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.MovingPiston;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMovingPiston extends MovingPiston implements BlockTemplate {
    
    public TemplateMovingPiston(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateMovingPiston(int id) {
        super(id);
    }
}
