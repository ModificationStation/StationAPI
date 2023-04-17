package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Bookshelf;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBookshelf extends Bookshelf implements BlockTemplate {

    public TemplateBookshelf(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBookshelf(int id, int texture) {
        super(id, texture);
    }
}
