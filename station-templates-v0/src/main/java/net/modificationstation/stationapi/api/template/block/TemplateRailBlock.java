package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RailBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRailBlock extends RailBlock implements BlockTemplate {
    public TemplateRailBlock(Identifier identifier, int j, boolean flag) {
        this(BlockTemplate.getNextId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRailBlock(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
