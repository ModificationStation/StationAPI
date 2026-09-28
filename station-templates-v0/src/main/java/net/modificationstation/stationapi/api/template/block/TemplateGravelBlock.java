package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.GravelBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateGravelBlock extends GravelBlock implements BlockTemplate {
    public TemplateGravelBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateGravelBlock(int id, int textureId) {
        super(id, textureId);
    }
}
