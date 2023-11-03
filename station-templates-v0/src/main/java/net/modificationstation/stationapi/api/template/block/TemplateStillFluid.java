package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Material;
import net.minecraft.block.StillLiquidBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStillFluid extends StillLiquidBlock implements BlockTemplate {

    public TemplateStillFluid(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStillFluid(int i, Material arg) {
        super(i, arg);
    }
}
