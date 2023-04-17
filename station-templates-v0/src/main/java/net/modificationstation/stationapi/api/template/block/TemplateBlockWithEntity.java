package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public abstract class TemplateBlockWithEntity extends BlockWithEntity implements BlockTemplate {

    public TemplateBlockWithEntity(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlockWithEntity(Identifier identifier, int tex, Material material) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), tex, material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBlockWithEntity(int i, Material arg) {
        super(i, arg);
    }

    public TemplateBlockWithEntity(int i, int j, Material arg) {
        super(i, j, arg);
    }
}
