package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LeavesBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLeavesBlock extends LeavesBlock implements BlockTemplate {
    public TemplateLeavesBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLeavesBlock(int i, int j) {
        super(i, j);
    }
}
