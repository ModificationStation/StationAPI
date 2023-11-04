package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Material;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.class_389;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePressurePlateBlock extends PressurePlateBlock implements BlockTemplate {
    public TemplatePressurePlateBlock(Identifier identifier, int j, class_389 arg, Material arg1) {
        this(BlockTemplate.getNextId(), j, arg, arg1);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePressurePlateBlock(int i, int j, class_389 arg, Material arg1) {
        super(i, j, arg, arg1);
    }
}
