package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RailBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRailBlock extends RailBlock implements BlockTemplate {
    public TemplateRailBlock(Identifier identifier, int textureId, boolean alwaysStraight) {
        this(BlockTemplate.getNextId(), textureId, alwaysStraight);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRailBlock(int id, int textureId, boolean alwaysStraight) {
        super(id, textureId, alwaysStraight);
    }
}
