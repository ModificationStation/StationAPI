package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PlantBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePlant extends PlantBlock implements BlockTemplate {

    public TemplatePlant(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePlant(int id, int texture) {
        super(id, texture);
    }
}
