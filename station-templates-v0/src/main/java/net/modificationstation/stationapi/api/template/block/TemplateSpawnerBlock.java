package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SpawnerBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSpawnerBlock extends SpawnerBlock implements BlockTemplate {
    public TemplateSpawnerBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSpawnerBlock(int id, int textureId) {
        super(id, textureId);
    }
}
