package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SandstoneBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSandstone extends SandstoneBlock implements BlockTemplate {
    
    public TemplateSandstone(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSandstone(int i) {
        super(i);
    }
}
