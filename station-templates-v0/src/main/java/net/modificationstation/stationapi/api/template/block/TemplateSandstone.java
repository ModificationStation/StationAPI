package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Sandstone;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSandstone extends Sandstone implements BlockTemplate {
    
    public TemplateSandstone(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSandstone(int i) {
        super(i);
    }
}
