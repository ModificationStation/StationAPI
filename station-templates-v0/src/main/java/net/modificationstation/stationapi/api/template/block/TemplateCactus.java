package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Cactus;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCactus extends Cactus implements BlockTemplate {

    public TemplateCactus(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCactus(int id, int texture) {
        super(id, texture);
    }
}
