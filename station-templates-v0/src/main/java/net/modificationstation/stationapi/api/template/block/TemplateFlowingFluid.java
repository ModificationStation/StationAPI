package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FlowingLiquidBlock;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFlowingFluid extends FlowingLiquidBlock implements BlockTemplate {

    public TemplateFlowingFluid(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFlowingFluid(int i, Material arg) {
        super(i, arg);
    }
}
