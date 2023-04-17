package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Plant;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePlant extends Plant implements BlockTemplate {

    public TemplatePlant(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePlant(int id, int texture) {
        super(id, texture);
    }
}
