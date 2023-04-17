package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneRepeater;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneRepeater extends RedstoneRepeater implements BlockTemplate {
    
    public TemplateRedstoneRepeater(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstoneRepeater(int i, boolean flag) {
        super(i, flag);
    }
}
