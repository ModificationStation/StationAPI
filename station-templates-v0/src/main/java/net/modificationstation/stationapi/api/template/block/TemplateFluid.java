package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Fluid;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFluid extends Fluid implements BlockTemplate {

    public TemplateFluid(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFluid(int i, Material arg) {
        super(i, arg);
    }
}
