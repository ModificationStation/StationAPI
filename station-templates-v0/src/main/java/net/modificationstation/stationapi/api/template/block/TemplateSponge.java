package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Sponge;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSponge extends Sponge implements BlockTemplate {
    
    public TemplateSponge(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSponge(int i) {
        super(i);
    }
}
