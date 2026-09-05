package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FenceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFenceBlock extends FenceBlock implements BlockTemplate {
    public TemplateFenceBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFenceBlock(int id, int textureId) {
        super(id, textureId);
    }
}
