package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.PressurePlateTrigger;
import net.minecraft.block.material.Material;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class PressurePlate extends net.minecraft.block.PressurePlate {
    
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
    public PressurePlate sounds(BlockSounds sounds) {
        return (PressurePlate) super.sounds(sounds);
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
    public PressurePlate setName(String string) {
        return (PressurePlate) super.setName(string);
    }

    @Override
    public PressurePlate disableStat() {
        return (PressurePlate) super.disableStat();
    }
}
