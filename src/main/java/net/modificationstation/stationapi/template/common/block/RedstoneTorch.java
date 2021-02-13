package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class RedstoneTorch extends net.minecraft.block.RedstoneTorch {
    
    public RedstoneTorch(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public RedstoneTorch(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public RedstoneTorch disableNotifyOnMetaDataChange() {
        return (RedstoneTorch) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public RedstoneTorch sounds(BlockSounds sounds) {
        return (RedstoneTorch) super.setSounds(sounds);
    }

    @Override
    public RedstoneTorch setLightOpacity(int i) {
        return (RedstoneTorch) super.setLightOpacity(i);
    }

    @Override
    public RedstoneTorch setLightEmittance(float f) {
        return (RedstoneTorch) super.setLightEmittance(f);
    }

    @Override
    public RedstoneTorch setBlastResistance(float resistance) {
        return (RedstoneTorch) super.setBlastResistance(resistance);
    }

    @Override
    public RedstoneTorch setHardness(float hardness) {
        return (RedstoneTorch) super.setHardness(hardness);
    }

    @Override
    public RedstoneTorch setUnbreakable() {
        return (RedstoneTorch) super.setUnbreakable();
    }

    @Override
    public RedstoneTorch setTicksRandomly(boolean ticksRandomly) {
        return (RedstoneTorch) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public RedstoneTorch setName(String string) {
        return (RedstoneTorch) super.setTranslationKey(string);
    }

    @Override
    public RedstoneTorch disableStat() {
        return (RedstoneTorch) super.disableStat();
    }
}
