package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Chest;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateChest extends Chest implements BlockTemplate {

    public TemplateChest(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateChest(int id) {
        super(id);
    }
}
