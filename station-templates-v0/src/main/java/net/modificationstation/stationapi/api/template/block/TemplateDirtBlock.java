package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DirtBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDirtBlock extends DirtBlock implements BlockTemplate {
    public TemplateDirtBlock(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDirtBlock(int id, int texture) {
        super(id, texture);
    }
}
