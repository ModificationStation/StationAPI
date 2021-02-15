package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class MovingPiston extends net.minecraft.block.MovingPiston implements IBlockTemplate<MovingPiston> {
    
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
    public MovingPiston setSounds(BlockSounds sounds) {
        return (MovingPiston) super.setSounds(sounds);
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
    public MovingPiston setTranslationKey(String string) {
        return (MovingPiston) super.setTranslationKey(string);
    }

    @Override
    public MovingPiston disableStat() {
        return (MovingPiston) super.disableStat();
    }
}
