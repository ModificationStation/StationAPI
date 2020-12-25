package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class FlowingFluid extends net.minecraft.block.FlowingFluid {

    public FlowingFluid(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public FlowingFluid(int i, Material arg) {
        super(i, arg);
    }

    @Override
    public FlowingFluid disableNotifyOnMetaDataChange() {
        return (FlowingFluid) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public FlowingFluid sounds(BlockSounds sounds) {
        return (FlowingFluid) super.sounds(sounds);
    }

    @Override
    public FlowingFluid setLightOpacity(int i) {
        return (FlowingFluid) super.setLightOpacity(i);
    }

    @Override
    public FlowingFluid setLightEmittance(float f) {
        return (FlowingFluid) super.setLightEmittance(f);
    }

    @Override
    public FlowingFluid setBlastResistance(float resistance) {
        return (FlowingFluid) super.setBlastResistance(resistance);
    }

    @Override
    public FlowingFluid setHardness(float hardness) {
        return (FlowingFluid) super.setHardness(hardness);
    }

    @Override
    public FlowingFluid setUnbreakable() {
        return (FlowingFluid) super.setUnbreakable();
    }

    @Override
    public FlowingFluid setTicksRandomly(boolean ticksRandomly) {
        return (FlowingFluid) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public FlowingFluid setName(String string) {
        return (FlowingFluid) super.setName(string);
    }

    @Override
    public FlowingFluid disableStat() {
        return (FlowingFluid) super.disableStat();
    }
}
