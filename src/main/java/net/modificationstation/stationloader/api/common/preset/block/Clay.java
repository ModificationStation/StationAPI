package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Clay extends net.minecraft.block.Clay {

    public Clay(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Clay(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Clay disableNotifyOnMetaDataChange() {
        return (Clay) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Clay sounds(BlockSounds sounds) {
        return (Clay) super.sounds(sounds);
    }

    @Override
    public Clay setLightOpacity(int i) {
        return (Clay) super.setLightOpacity(i);
    }

    @Override
    public Clay setLightEmittance(float f) {
        return (Clay) super.setLightEmittance(f);
    }

    @Override
    public Clay setBlastResistance(float resistance) {
        return (Clay) super.setBlastResistance(resistance);
    }

    @Override
    public Clay setHardness(float hardness) {
        return (Clay) super.setHardness(hardness);
    }

    @Override
    public Clay setUnbreakable() {
        return (Clay) super.setUnbreakable();
    }
}