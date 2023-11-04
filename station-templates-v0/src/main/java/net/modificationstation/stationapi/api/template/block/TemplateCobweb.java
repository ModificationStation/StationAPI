package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CobwebBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCobweb extends CobwebBlock implements BlockTemplate {

    public TemplateCobweb(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCobweb(int i, int j) {
        super(i, j);
    }
}
