package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PressurePlateActivationRule;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePressurePlateBlock extends PressurePlateBlock implements BlockTemplate {
    public TemplatePressurePlateBlock(Identifier identifier, int textureId, PressurePlateActivationRule activationRule, Material material) {
        this(BlockTemplate.getNextId(), textureId, activationRule, material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePressurePlateBlock(int id, int textureId, PressurePlateActivationRule activationRule, Material material) {
        super(id, textureId, activationRule, material);
    }
}
