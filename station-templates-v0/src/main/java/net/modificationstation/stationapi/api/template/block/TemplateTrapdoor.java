package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Material;
import net.minecraft.block.TrapdoorBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTrapdoor extends TrapdoorBlock implements BlockTemplate {
    
    public TemplateTrapdoor(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTrapdoor(int i, Material arg) {
        super(i, arg);
    }
}
