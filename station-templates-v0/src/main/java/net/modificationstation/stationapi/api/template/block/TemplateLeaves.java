package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LeavesBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateLeaves extends LeavesBlock implements BlockTemplate {

    public TemplateLeaves(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLeaves(int i, int j) {
        super(i, j);
    }
}
