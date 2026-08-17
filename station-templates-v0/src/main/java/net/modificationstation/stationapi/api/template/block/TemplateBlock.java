package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBlock extends Block implements BlockTemplate {
    public TemplateBlock(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlock(Identifier identifier, int textureId, Material material) {
        this(BlockTemplate.getNextId(), textureId, material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlock(int id, Material material) {
        super(id, material);
    }

    public TemplateBlock(int id, int textureId, Material material) {
        super(id, textureId, material);
    }
}
