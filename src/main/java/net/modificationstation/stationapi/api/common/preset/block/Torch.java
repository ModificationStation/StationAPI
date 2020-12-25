package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Torch extends net.minecraft.block.Torch {
    
    public Torch(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Torch(int i, int j) {
        super(i, j);
    }

    @Override
    public Torch disableNotifyOnMetaDataChange() {
        return (Torch) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Torch sounds(BlockSounds sounds) {
        return (Torch) super.sounds(sounds);
    }

    @Override
    public Torch setLightOpacity(int i) {
        return (Torch) super.setLightOpacity(i);
    }

    @Override
    public Torch setLightEmittance(float f) {
        return (Torch) super.setLightEmittance(f);
    }

    @Override
    public Torch setBlastResistance(float resistance) {
        return (Torch) super.setBlastResistance(resistance);
    }

    @Override
    public Torch setHardness(float hardness) {
        return (Torch) super.setHardness(hardness);
    }

    @Override
    public Torch setUnbreakable() {
        return (Torch) super.setUnbreakable();
    }

    @Override
    public Torch setTicksRandomly(boolean ticksRandomly) {
        return (Torch) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Torch setName(String string) {
        return (Torch) super.setName(string);
    }

    @Override
    public Torch disableStat() {
        return (Torch) super.disableStat();
    }
}
