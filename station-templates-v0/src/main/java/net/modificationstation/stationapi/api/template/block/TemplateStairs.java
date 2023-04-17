package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Stairs;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStairs extends Stairs implements BlockTemplate {
    
    public TemplateStairs(Identifier identifier, BlockBase arg) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStairs(int i, BlockBase arg) {
        super(i, arg);
    }
}
