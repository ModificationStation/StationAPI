package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LeverBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLeverBlock extends LeverBlock implements BlockTemplate {
    public TemplateLeverBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLeverBlock(int i, int j) {
        super(i, j);
    }
}
