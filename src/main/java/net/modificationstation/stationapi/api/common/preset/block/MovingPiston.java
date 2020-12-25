package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class MovingPiston extends net.minecraft.block.MovingPiston {
    
    public MovingPiston(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public MovingPiston(int id) {
        super(id);
    }

    @Override
    public MovingPiston disableNotifyOnMetaDataChange() {
        return (MovingPiston) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public MovingPiston sounds(BlockSounds sounds) {
        return (MovingPiston) super.sounds(sounds);
    }

    @Override
    public MovingPiston setLightOpacity(int i) {
        return (MovingPiston) super.setLightOpacity(i);
    }

    @Override
    public MovingPiston setLightEmittance(float f) {
        return (MovingPiston) super.setLightEmittance(f);
    }

    @Override
    public MovingPiston setBlastResistance(float resistance) {
        return (MovingPiston) super.setBlastResistance(resistance);
    }

    @Override
    public MovingPiston setHardness(float hardness) {
        return (MovingPiston) super.setHardness(hardness);
    }

    @Override
    public MovingPiston setUnbreakable() {
        return (MovingPiston) super.setUnbreakable();
    }

    @Override
    public MovingPiston setTicksRandomly(boolean ticksRandomly) {
        return (MovingPiston) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public MovingPiston setName(String string) {
        return (MovingPiston) super.setName(string);
    }

    @Override
    public MovingPiston disableStat() {
        return (MovingPiston) super.disableStat();
    }
}
