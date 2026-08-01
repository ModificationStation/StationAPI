package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTrapdoorBlock extends TrapdoorBlock implements BlockTemplate {
    public TemplateTrapdoorBlock(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateTrapdoorBlock(int id, Material material) {
        super(id, material);
    }
}
