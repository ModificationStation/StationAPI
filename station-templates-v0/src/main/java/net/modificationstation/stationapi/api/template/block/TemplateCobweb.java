package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Cobweb;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCobweb extends Cobweb implements BlockTemplate {

    public TemplateCobweb(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCobweb(int i, int j) {
        super(i, j);
    }
}
