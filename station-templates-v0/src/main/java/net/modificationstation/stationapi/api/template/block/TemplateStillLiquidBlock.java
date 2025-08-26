package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.StillLiquidBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateStillLiquidBlock extends StillLiquidBlock implements BlockTemplate {
    public TemplateStillLiquidBlock(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStillLiquidBlock(int i, Material arg) {
        super(i, arg);
    }
}
