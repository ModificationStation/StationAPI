package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStairs extends StairsBlock implements BlockTemplate {
    
    public TemplateStairs(Identifier identifier, Block arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStairs(int i, Block arg) {
        super(i, arg);
    }
}
