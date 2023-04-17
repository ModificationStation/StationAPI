package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Bed;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBed extends Bed implements BlockTemplate {

    public TemplateBed(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateBed(int id) {
        super(id);
    }
}
