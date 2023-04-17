package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Dirt;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDirt extends Dirt implements BlockTemplate {

    public TemplateDirt(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDirt(int id, int texture) {
        super(id, texture);
    }
}
