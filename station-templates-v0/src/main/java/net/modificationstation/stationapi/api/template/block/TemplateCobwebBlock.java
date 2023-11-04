package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CobwebBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCobwebBlock extends CobwebBlock implements BlockTemplate {
    public TemplateCobwebBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCobwebBlock(int i, int j) {
        super(i, j);
    }
}
