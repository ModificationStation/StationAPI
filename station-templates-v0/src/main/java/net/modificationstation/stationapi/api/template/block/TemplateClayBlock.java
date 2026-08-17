package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.ClayBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateClayBlock extends ClayBlock implements BlockTemplate {
    public TemplateClayBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateClayBlock(int id, int textureId) {
        super(id, textureId);
    }
}
