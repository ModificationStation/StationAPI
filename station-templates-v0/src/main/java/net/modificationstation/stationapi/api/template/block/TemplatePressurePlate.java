package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePressurePlate extends net.minecraft.block.PressurePlate implements BlockTemplate<TemplatePressurePlate> {

    public TemplatePressurePlate(Identifier identifier, int j, PressurePlateTrigger arg, Material arg1) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j, arg, arg1);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplatePressurePlate(int i, int j, PressurePlateTrigger arg, Material arg1) {
        super(i, j, arg, arg1);
    }

    @Override
    public TemplatePressurePlate disableNotifyOnMetaDataChange() {
        return (TemplatePressurePlate) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplatePressurePlate setSounds(BlockSounds sounds) {
        return (TemplatePressurePlate) super.setSounds(sounds);
    }

    @Override
    public TemplatePressurePlate setLightOpacity(int i) {
        return (TemplatePressurePlate) super.setLightOpacity(i);
    }

    @Override
    public TemplatePressurePlate setLightEmittance(float f) {
        return (TemplatePressurePlate) super.setLightEmittance(f);
    }

    @Override
    public TemplatePressurePlate setBlastResistance(float resistance) {
        return (TemplatePressurePlate) super.setBlastResistance(resistance);
    }

    @Override
    public TemplatePressurePlate setHardness(float hardness) {
        return (TemplatePressurePlate) super.setHardness(hardness);
    }

    @Override
    public TemplatePressurePlate setUnbreakable() {
        return (TemplatePressurePlate) super.setUnbreakable();
    }

    @Override
    public TemplatePressurePlate setTicksRandomly(boolean ticksRandomly) {
        return (TemplatePressurePlate) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplatePressurePlate setTranslationKey(String string) {
        return (TemplatePressurePlate) super.setTranslationKey(string);
    }

    @Override
    public TemplatePressurePlate disableStat() {
        return (TemplatePressurePlate) super.disableStat();
    }
}
