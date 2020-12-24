package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Stone extends net.minecraft.block.Stone {
    
    public Stone(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Stone(int i, int j) {
        super(i, j);
    }

    @Override
    public Stone disableNotifyOnMetaDataChange() {
        return (Stone) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Stone sounds(BlockSounds sounds) {
        return (Stone) super.sounds(sounds);
    }

    @Override
    public Stone setLightOpacity(int i) {
        return (Stone) super.setLightOpacity(i);
    }

    @Override
    public Stone setLightEmittance(float f) {
        return (Stone) super.setLightEmittance(f);
    }

    @Override
    public Stone setBlastResistance(float resistance) {
        return (Stone) super.setBlastResistance(resistance);
    }

    @Override
    public Stone setHardness(float hardness) {
        return (Stone) super.setHardness(hardness);
    }

    @Override
    public Stone setUnbreakable() {
        return (Stone) super.setUnbreakable();
    }
}