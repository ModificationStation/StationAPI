package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneWireBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneDust extends RedstoneWireBlock implements BlockTemplate {

    public TemplateRedstoneDust(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRedstoneDust(int i, int j) {
        super(i, j);
    }
}
