package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Material;
import net.minecraft.block.TrapdoorBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTrapdoorBlock extends TrapdoorBlock implements BlockTemplate {
    public TemplateTrapdoorBlock(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTrapdoorBlock(int i, Material arg) {
        super(i, arg);
    }
}
