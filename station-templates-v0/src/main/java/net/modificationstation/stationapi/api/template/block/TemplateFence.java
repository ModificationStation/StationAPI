package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FenceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFence extends FenceBlock implements BlockTemplate {

    public TemplateFence(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFence(int i, int j) {
        super(i, j);
    }
}
