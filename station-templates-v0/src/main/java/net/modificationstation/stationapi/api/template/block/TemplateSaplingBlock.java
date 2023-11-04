package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SaplingBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSaplingBlock extends SaplingBlock implements BlockTemplate {
    public TemplateSaplingBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSaplingBlock(int i, int j) {
        super(i, j);
    }
}
