package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TranslucentBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTranslucentBlock extends TranslucentBlock implements BlockTemplate {
    public TemplateTranslucentBlock(Identifier identifier, int textureId, Material material, boolean transparent) {
        this(BlockTemplate.getNextId(), textureId, material, transparent);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateTranslucentBlock(int id, int textureId, Material material, boolean transparent) {
        super(id, textureId, material, transparent);
    }
}
