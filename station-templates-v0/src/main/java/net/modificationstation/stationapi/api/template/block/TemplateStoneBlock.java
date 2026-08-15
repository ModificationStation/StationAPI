package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.StoneBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateStoneBlock extends StoneBlock implements BlockTemplate {
    public TemplateStoneBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateStoneBlock(int id, int textureId) {
        super(id, textureId);
    }
}
