package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneOre;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneOre extends RedstoneOre implements BlockTemplate {
    
    public TemplateRedstoneOre(Identifier identifier, int j, boolean isLit) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j, isLit);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateRedstoneOre(int i, int j, boolean isLit) {
        super(i, j, isLit);
    }
}
