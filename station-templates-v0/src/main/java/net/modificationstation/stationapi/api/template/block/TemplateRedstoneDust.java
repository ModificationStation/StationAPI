package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneDust;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneDust extends RedstoneDust implements BlockTemplate {

    public TemplateRedstoneDust(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRedstoneDust(int i, int j) {
        super(i, j);
    }
}
