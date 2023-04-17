package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Tnt;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTnt extends Tnt implements BlockTemplate {
    
    public TemplateTnt(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTnt(int i, int j) {
        super(i, j);
    }
}
