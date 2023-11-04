package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.IceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateIceBlock extends IceBlock implements BlockTemplate {
    public TemplateIceBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateIceBlock(int i, int j) {
        super(i, j);
    }
}
