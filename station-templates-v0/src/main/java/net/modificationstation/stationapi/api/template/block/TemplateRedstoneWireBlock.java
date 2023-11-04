package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneWireBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRedstoneWireBlock extends RedstoneWireBlock implements BlockTemplate {
    public TemplateRedstoneWireBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRedstoneWireBlock(int i, int j) {
        super(i, j);
    }
}
