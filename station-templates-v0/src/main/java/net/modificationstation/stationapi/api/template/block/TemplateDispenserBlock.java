package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DispenserBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDispenserBlock extends DispenserBlock implements BlockTemplate {
    public TemplateDispenserBlock(Identifier identifier) {
        this(BlockTemplate.getNextId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDispenserBlock(int id) {
        super(id);
    }
}
