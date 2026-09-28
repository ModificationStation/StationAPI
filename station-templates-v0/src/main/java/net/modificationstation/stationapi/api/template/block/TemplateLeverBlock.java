package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LeverBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLeverBlock extends LeverBlock implements BlockTemplate {
    public TemplateLeverBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLeverBlock(int id, int textureId) {
        super(id, textureId);
    }
}
