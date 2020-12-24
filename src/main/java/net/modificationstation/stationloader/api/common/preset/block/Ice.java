package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Ice extends net.minecraft.block.Ice {

    public Ice(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Ice(int i, int j) {
        super(i, j);
    }

    @Override
    public Ice disableNotifyOnMetaDataChange() {
        return (Ice) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Ice sounds(BlockSounds sounds) {
        return (Ice) super.sounds(sounds);
    }

    @Override
    public Ice setLightOpacity(int i) {
        return (Ice) super.setLightOpacity(i);
    }

    @Override
    public Ice setLightEmittance(float f) {
        return (Ice) super.setLightEmittance(f);
    }

    @Override
    public Ice setBlastResistance(float resistance) {
        return (Ice) super.setBlastResistance(resistance);
    }

    @Override
    public Ice setHardness(float hardness) {
        return (Ice) super.setHardness(hardness);
    }

    @Override
    public Ice setUnbreakable() {
        return (Ice) super.setUnbreakable();
    }
}
