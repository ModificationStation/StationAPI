package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Tnt extends net.minecraft.block.Tnt {
    
    public Tnt(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Tnt(int i, int j) {
        super(i, j);
    }

    @Override
    public Tnt disableNotifyOnMetaDataChange() {
        return (Tnt) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Tnt sounds(BlockSounds sounds) {
        return (Tnt) super.sounds(sounds);
    }

    @Override
    public Tnt setLightOpacity(int i) {
        return (Tnt) super.setLightOpacity(i);
    }

    @Override
    public Tnt setLightEmittance(float f) {
        return (Tnt) super.setLightEmittance(f);
    }

    @Override
    public Tnt setBlastResistance(float resistance) {
        return (Tnt) super.setBlastResistance(resistance);
    }

    @Override
    public Tnt setHardness(float hardness) {
        return (Tnt) super.setHardness(hardness);
    }

    @Override
    public Tnt setUnbreakable() {
        return (Tnt) super.setUnbreakable();
    }

    @Override
    public Tnt setTicksRandomly(boolean ticksRandomly) {
        return (Tnt) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Tnt setName(String string) {
        return (Tnt) super.setName(string);
    }

    @Override
    public Tnt disableStat() {
        return (Tnt) super.disableStat();
    }
}
