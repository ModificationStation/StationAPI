package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Material;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.class_389;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePressurePlate extends PressurePlateBlock implements BlockTemplate {

    public TemplatePressurePlate(Identifier identifier, int j, class_389 arg, Material arg1) {
        this(BlockTemplate.getNextId(), j, arg, arg1);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePressurePlate(int i, int j, class_389 arg, Material arg1) {
        super(i, j, arg, arg1);
    }
}
