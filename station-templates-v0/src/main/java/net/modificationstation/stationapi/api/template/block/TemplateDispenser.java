package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Dispenser;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDispenser extends Dispenser implements BlockTemplate {

    public TemplateDispenser(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDispenser(int id) {
        super(id);
    }
}
