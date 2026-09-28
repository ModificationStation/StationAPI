package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SoulSandBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSoulSandBlock extends SoulSandBlock implements BlockTemplate {
    public TemplateSoulSandBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSoulSandBlock(int id, int textureId) {
        super(id, textureId);
    }
}
