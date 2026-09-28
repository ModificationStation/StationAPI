package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.NetherrackBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateNetherrackBlock extends NetherrackBlock implements BlockTemplate {
    public TemplateNetherrackBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateNetherrackBlock(int id, int textureId) {
        super(id, textureId);
    }
}
