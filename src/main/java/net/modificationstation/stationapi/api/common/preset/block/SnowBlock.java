package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class SnowBlock extends net.minecraft.block.SnowBlock {
    
    public SnowBlock(Identifier identifier, int texUVStart) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texUVStart);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public SnowBlock(int id, int texUVStart) {
        super(id, texUVStart);
    }

    @Override
    public SnowBlock disableNotifyOnMetaDataChange() {
        return (SnowBlock) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public SnowBlock sounds(BlockSounds sounds) {
        return (SnowBlock) super.sounds(sounds);
    }

    @Override
    public SnowBlock setLightOpacity(int i) {
        return (SnowBlock) super.setLightOpacity(i);
    }

    @Override
    public SnowBlock setLightEmittance(float f) {
        return (SnowBlock) super.setLightEmittance(f);
    }

    @Override
    public SnowBlock setBlastResistance(float resistance) {
        return (SnowBlock) super.setBlastResistance(resistance);
    }

    @Override
    public SnowBlock setHardness(float hardness) {
        return (SnowBlock) super.setHardness(hardness);
    }

    @Override
    public SnowBlock setUnbreakable() {
        return (SnowBlock) super.setUnbreakable();
    }

    @Override
    public SnowBlock setTicksRandomly(boolean ticksRandomly) {
        return (SnowBlock) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public SnowBlock setName(String string) {
        return (SnowBlock) super.setName(string);
    }

    @Override
    public SnowBlock disableStat() {
        return (SnowBlock) super.disableStat();
    }
}
