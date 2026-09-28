package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.IceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateIceBlock extends IceBlock implements BlockTemplate {
    public TemplateIceBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateIceBlock(int id, int textureId) {
        super(id, textureId);
    }
}
