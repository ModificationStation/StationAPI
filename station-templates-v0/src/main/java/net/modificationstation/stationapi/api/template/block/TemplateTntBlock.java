package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TntBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTntBlock extends TntBlock implements BlockTemplate {
    public TemplateTntBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateTntBlock(int id, int textureId) {
        super(id, textureId);
    }
}
