package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CakeBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCakeBlock extends CakeBlock implements BlockTemplate {
    public TemplateCakeBlock(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCakeBlock(int id, int texture) {
        super(id, texture);
    }
}
