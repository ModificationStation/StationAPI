package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CactusBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCactusBlock extends CactusBlock implements BlockTemplate {
    public TemplateCactusBlock(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCactusBlock(int id, int texture) {
        super(id, texture);
    }
}
