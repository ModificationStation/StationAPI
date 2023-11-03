package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SpongeBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSponge extends SpongeBlock implements BlockTemplate {
    
    public TemplateSponge(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSponge(int i) {
        super(i);
    }
}
