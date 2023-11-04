package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GlowstoneBlock;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGlowstone extends GlowstoneBlock implements BlockTemplate {
    
    public TemplateGlowstone(Identifier identifier, int j, Material arg) {
        this(BlockTemplate.getNextId(), j, arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateGlowstone(int i, int j, Material arg) {
        super(i, j, arg);
    }
}
