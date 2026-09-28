package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GlassBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGlassBlock extends GlassBlock implements BlockTemplate {
    public TemplateGlassBlock(Identifier identifier, int textureId, Material material, boolean transparent) {
        this(BlockTemplate.getNextId(), textureId, material, transparent);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateGlassBlock(int id, int textureId, Material material, boolean transparent) {
        super(id, textureId, material, transparent);
    }
}
