package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BookshelfBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBookshelfBlock extends BookshelfBlock implements BlockTemplate {
    public TemplateBookshelfBlock(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBookshelfBlock(int id, int texture) {
        super(id, texture);
    }
}
