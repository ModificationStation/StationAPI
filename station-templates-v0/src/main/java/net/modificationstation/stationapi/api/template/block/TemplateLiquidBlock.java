package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateLiquidBlock extends LiquidBlock implements BlockTemplate {
    public TemplateLiquidBlock(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLiquidBlock(int i, Material arg) {
        super(i, arg);
    }
}
