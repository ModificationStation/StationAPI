package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BookshelfBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateBookshelf extends BookshelfBlock implements BlockTemplate {

    public TemplateBookshelf(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBookshelf(int id, int texture) {
        super(id, texture);
    }
}
