package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.IceBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateIce extends IceBlock implements BlockTemplate {

    public TemplateIce(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateIce(int i, int j) {
        super(i, j);
    }
}
