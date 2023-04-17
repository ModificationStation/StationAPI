package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Furnace;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFurnace extends Furnace implements BlockTemplate {

    public TemplateFurnace(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFurnace(int i, boolean flag) {
        super(i, flag);
    }
}
