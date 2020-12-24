package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

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
}
