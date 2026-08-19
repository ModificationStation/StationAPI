package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SnowBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSnowBlock extends SnowBlock implements BlockTemplate {
    public TemplateSnowBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSnowBlock(int id, int textureId) {
        super(id, textureId);
    }
}
