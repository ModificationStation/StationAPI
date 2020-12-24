package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

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
    public Log sounds(BlockSounds sounds) {
        return (Log) super.sounds(sounds);
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
}