package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Grass;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateGrass extends Grass implements BlockTemplate {

    public TemplateGrass(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateGrass(int id) {
        super(id);
    }
}
