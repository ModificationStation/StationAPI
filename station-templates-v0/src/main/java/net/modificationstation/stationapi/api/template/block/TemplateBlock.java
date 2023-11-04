package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBlock extends Block implements BlockTemplate {
    public TemplateBlock(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlock(Identifier identifier, int tex, Material material) {
        this(BlockTemplate.getNextId(), tex, material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlock(int id, Material material) {
        super(id, material);
    }

    public TemplateBlock(int id, int tex, Material material) {
        super(id, tex, material);
    }
}
