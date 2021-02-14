package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Log extends net.minecraft.block.Log {
    
    public Log(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Log(int i) {
        super(i);
    }

    @Override
    public Log disableNotifyOnMetaDataChange() {
        return (Log) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Log setSounds(BlockSounds sounds) {
        return (Log) super.setSounds(sounds);
    }

    @Override
    public Log setLightOpacity(int i) {
        return (Log) super.setLightOpacity(i);
    }

    @Override
    public Log setLightEmittance(float f) {
        return (Log) super.setLightEmittance(f);
    }

    @Override
    public Log setBlastResistance(float resistance) {
        return (Log) super.setBlastResistance(resistance);
    }

    @Override
    public Log setHardness(float hardness) {
        return (Log) super.setHardness(hardness);
    }

    @Override
    public Log setUnbreakable() {
        return (Log) super.setUnbreakable();
    }

    @Override
    public Log setTicksRandomly(boolean ticksRandomly) {
        return (Log) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Log setTranslationKey(String string) {
        return (Log) super.setTranslationKey(string);
    }

    @Override
    public Log disableStat() {
        return (Log) super.disableStat();
    }
}
