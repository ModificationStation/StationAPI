package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Piston extends net.minecraft.block.Piston {

    public Piston(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Piston(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public Piston disableNotifyOnMetaDataChange() {
        return (Piston) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Piston sounds(BlockSounds sounds) {
        return (Piston) super.sounds(sounds);
    }

    @Override
    public Piston setLightOpacity(int i) {
        return (Piston) super.setLightOpacity(i);
    }

    @Override
    public Piston setLightEmittance(float f) {
        return (Piston) super.setLightEmittance(f);
    }

    @Override
    public Piston setBlastResistance(float resistance) {
        return (Piston) super.setBlastResistance(resistance);
    }

    @Override
    public Piston setHardness(float hardness) {
        return (Piston) super.setHardness(hardness);
    }

    @Override
    public Piston setUnbreakable() {
        return (Piston) super.setUnbreakable();
    }
}
