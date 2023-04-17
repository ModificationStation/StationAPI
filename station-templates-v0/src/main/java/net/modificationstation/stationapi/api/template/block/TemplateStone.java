package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Stone;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStone extends Stone implements BlockTemplate {
    
    public TemplateStone(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStone(int i, int j) {
        super(i, j);
    }
}
