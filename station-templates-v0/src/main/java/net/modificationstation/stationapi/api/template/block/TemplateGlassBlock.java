package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GlassBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGlassBlock extends GlassBlock implements BlockTemplate {
    public TemplateGlassBlock(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockTemplate.getNextId(), j, arg, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateGlassBlock(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }
}
