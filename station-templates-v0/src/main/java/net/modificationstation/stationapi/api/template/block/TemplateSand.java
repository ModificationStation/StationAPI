package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Sand;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSand extends Sand implements BlockTemplate {
    
    public TemplateSand(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSand(int i, int j) {
        super(i, j);
    }
}
