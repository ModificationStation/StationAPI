package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public abstract class TemplateBlockWithEntity extends BlockWithEntity implements BlockTemplate {
    public TemplateBlockWithEntity(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlockWithEntity(Identifier identifier, int tex, Material material) {
        this(BlockTemplate.getNextId(), tex, material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlockWithEntity(int id, Material material) {
        super(id, material);
    }

    public TemplateBlockWithEntity(int id, int textureId, Material material) {
        super(id, textureId, material);
    }
}
