package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DispenserBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDispenser extends DispenserBlock implements BlockTemplate {

    public TemplateDispenser(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDispenser(int id) {
        super(id);
    }
}
