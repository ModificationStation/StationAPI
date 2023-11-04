package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FenceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFenceBlock extends FenceBlock implements BlockTemplate {
    public TemplateFenceBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFenceBlock(int i, int j) {
        super(i, j);
    }
}
