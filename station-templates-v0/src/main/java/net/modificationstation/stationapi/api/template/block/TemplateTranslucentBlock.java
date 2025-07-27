package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TranslucentBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTranslucentBlock extends TranslucentBlock implements BlockTemplate {
    public TemplateTranslucentBlock(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockTemplate.getNextId(), j, arg, flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTranslucentBlock(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }
}
