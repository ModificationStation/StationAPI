package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Fire;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFire extends Fire implements BlockTemplate {

    public TemplateFire(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFire(int id, int texture) {
        super(id, texture);
    }
}
