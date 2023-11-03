package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DirtBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDirt extends DirtBlock implements BlockTemplate {

    public TemplateDirt(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDirt(int id, int texture) {
        super(id, texture);
    }
}
