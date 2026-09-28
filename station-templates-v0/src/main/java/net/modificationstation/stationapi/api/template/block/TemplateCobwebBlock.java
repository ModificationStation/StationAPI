package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CobwebBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCobwebBlock extends CobwebBlock implements BlockTemplate {
    public TemplateCobwebBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCobwebBlock(int id, int textureId) {
        super(id, textureId);
    }
}
