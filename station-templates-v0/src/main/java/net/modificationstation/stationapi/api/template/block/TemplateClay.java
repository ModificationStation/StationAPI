package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Clay;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateClay extends Clay implements BlockTemplate {

    public TemplateClay(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateClay(int id, int texture) {
        super(id, texture);
    }
}
