package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSandBlock extends SandBlock implements BlockTemplate {
    public TemplateSandBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSandBlock(int id, int textureId) {
        super(id, textureId);
    }
}
