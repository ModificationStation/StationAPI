package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.LiquidBlock;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFluid extends LiquidBlock implements BlockTemplate {

    public TemplateFluid(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFluid(int i, Material arg) {
        super(i, arg);
    }
}
