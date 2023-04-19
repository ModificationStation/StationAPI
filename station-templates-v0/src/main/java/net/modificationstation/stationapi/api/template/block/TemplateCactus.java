package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Cactus;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCactus extends Cactus implements BlockTemplate {

    public TemplateCactus(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCactus(int id, int texture) {
        super(id, texture);
    }
}
