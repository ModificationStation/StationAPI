package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Pumpkin;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePumpkin extends Pumpkin implements BlockTemplate {

    public TemplatePumpkin(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePumpkin(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
