package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TranslucentBlock extends net.minecraft.block.TranslucentBlock {
    
    public TranslucentBlock(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, arg, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TranslucentBlock(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }

    @Override
    public TranslucentBlock disableNotifyOnMetaDataChange() {
        return (TranslucentBlock) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TranslucentBlock sounds(BlockSounds sounds) {
        return (TranslucentBlock) super.sounds(sounds);
    }

    @Override
    public TranslucentBlock setLightOpacity(int i) {
        return (TranslucentBlock) super.setLightOpacity(i);
    }

    @Override
    public TranslucentBlock setLightEmittance(float f) {
        return (TranslucentBlock) super.setLightEmittance(f);
    }

    @Override
    public TranslucentBlock setBlastResistance(float resistance) {
        return (TranslucentBlock) super.setBlastResistance(resistance);
    }

    @Override
    public TranslucentBlock setHardness(float hardness) {
        return (TranslucentBlock) super.setHardness(hardness);
    }

    @Override
    public TranslucentBlock setUnbreakable() {
        return (TranslucentBlock) super.setUnbreakable();
    }

    @Override
    public TranslucentBlock setTicksRandomly(boolean ticksRandomly) {
        return (TranslucentBlock) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TranslucentBlock setName(String string) {
        return (TranslucentBlock) super.setName(string);
    }

    @Override
    public TranslucentBlock disableStat() {
        return (TranslucentBlock) super.disableStat();
    }
}
