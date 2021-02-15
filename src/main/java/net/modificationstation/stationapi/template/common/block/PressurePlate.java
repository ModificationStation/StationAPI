package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class PressurePlate extends net.minecraft.block.PressurePlate implements IBlockTemplate<PressurePlate> {
    
    public PressurePlate(Identifier identifier, int j, PressurePlateTrigger arg, Material arg1) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, arg, arg1);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public PressurePlate(int i, int j, PressurePlateTrigger arg, Material arg1) {
        super(i, j, arg, arg1);
    }

    @Override
    public PressurePlate disableNotifyOnMetaDataChange() {
        return (PressurePlate) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public PressurePlate setSounds(BlockSounds sounds) {
        return (PressurePlate) super.setSounds(sounds);
    }

    @Override
    public PressurePlate setLightOpacity(int i) {
        return (PressurePlate) super.setLightOpacity(i);
    }

    @Override
    public PressurePlate setLightEmittance(float f) {
        return (PressurePlate) super.setLightEmittance(f);
    }

    @Override
    public PressurePlate setBlastResistance(float resistance) {
        return (PressurePlate) super.setBlastResistance(resistance);
    }

    @Override
    public PressurePlate setHardness(float hardness) {
        return (PressurePlate) super.setHardness(hardness);
    }

    @Override
    public PressurePlate setUnbreakable() {
        return (PressurePlate) super.setUnbreakable();
    }

    @Override
    public PressurePlate setTicksRandomly(boolean ticksRandomly) {
        return (PressurePlate) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public PressurePlate setTranslationKey(String string) {
        return (PressurePlate) super.setTranslationKey(string);
    }

    @Override
    public PressurePlate disableStat() {
        return (PressurePlate) super.disableStat();
    }
}
