package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PressurePlate;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePressurePlate extends PressurePlate implements BlockTemplate {

    public TemplatePressurePlate(Identifier identifier, int j, PressurePlateTrigger arg, Material arg1) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j, arg, arg1);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePressurePlate(int i, int j, PressurePlateTrigger arg, Material arg1) {
        super(i, j, arg, arg1);
    }
}
