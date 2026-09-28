package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GlowstoneBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGlowstoneBlock extends GlowstoneBlock implements BlockTemplate {
    public TemplateGlowstoneBlock(Identifier identifier, int textureId, Material material) {
        this(BlockTemplate.getNextId(), textureId, material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateGlowstoneBlock(int id, int textureId, Material material) {
        super(id, textureId, material);
    }
}
