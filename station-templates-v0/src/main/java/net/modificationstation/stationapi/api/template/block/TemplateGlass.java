package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GlassBlock;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGlass extends GlassBlock implements BlockTemplate {

    public TemplateGlass(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockTemplate.getNextId(), j, arg, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateGlass(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }
}
