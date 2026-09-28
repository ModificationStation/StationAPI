package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DirtBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDirtBlock extends DirtBlock implements BlockTemplate {
    public TemplateDirtBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDirtBlock(int id, int textureId) {
        super(id, textureId);
    }
}
