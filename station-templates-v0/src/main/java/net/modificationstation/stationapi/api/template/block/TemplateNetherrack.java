package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.NetherrackBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateNetherrack extends NetherrackBlock implements BlockTemplate {

    public TemplateNetherrack(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateNetherrack(int i, int j) {
        super(i, j);
    }
}
