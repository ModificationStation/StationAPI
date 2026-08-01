package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.TorchBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateTorchBlock extends TorchBlock implements BlockTemplate {
    public TemplateTorchBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateTorchBlock(int id, int textureId) {
        super(id, textureId);
    }
}
