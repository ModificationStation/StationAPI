package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GlowstoneBlock;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGlowstoneBlock extends GlowstoneBlock implements BlockTemplate {
    public TemplateGlowstoneBlock(Identifier identifier, int j, Material arg) {
        this(BlockTemplate.getNextId(), j, arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateGlowstoneBlock(int i, int j, Material arg) {
        super(i, j, arg);
    }
}
