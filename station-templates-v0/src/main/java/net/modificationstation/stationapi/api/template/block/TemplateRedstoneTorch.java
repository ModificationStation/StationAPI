package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneTorch;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneTorch extends RedstoneTorch implements BlockTemplate {
    
    public TemplateRedstoneTorch(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j, flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstoneTorch(int i, int j, boolean flag) {
        super(i, j, flag);
    }
}
