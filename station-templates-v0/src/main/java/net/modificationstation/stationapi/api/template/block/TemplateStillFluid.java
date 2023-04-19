package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.StillFluid;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStillFluid extends StillFluid implements BlockTemplate {

    public TemplateStillFluid(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateStillFluid(int i, Material arg) {
        super(i, arg);
    }
}
