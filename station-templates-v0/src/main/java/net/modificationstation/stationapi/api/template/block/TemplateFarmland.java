package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Farmland;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFarmland extends Farmland implements BlockTemplate {

    public TemplateFarmland(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFarmland(int id) {
        super(id);
    }
}
