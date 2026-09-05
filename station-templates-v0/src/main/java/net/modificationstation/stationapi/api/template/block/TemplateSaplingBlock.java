package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SaplingBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSaplingBlock extends SaplingBlock implements BlockTemplate {
    public TemplateSaplingBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSaplingBlock(int id, int textureId) {
        super(id, textureId);
    }
}
