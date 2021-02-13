package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class MaterialBlock extends net.minecraft.block.MaterialBlock {
    
    public MaterialBlock(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public MaterialBlock(int i, int j) {
        super(i, j);
    }

    @Override
    public MaterialBlock disableNotifyOnMetaDataChange() {
        return (MaterialBlock) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public MaterialBlock sounds(BlockSounds sounds) {
        return (MaterialBlock) super.setSounds(sounds);
    }

    @Override
    public MaterialBlock setLightOpacity(int i) {
        return (MaterialBlock) super.setLightOpacity(i);
    }

    @Override
    public MaterialBlock setLightEmittance(float f) {
        return (MaterialBlock) super.setLightEmittance(f);
    }

    @Override
    public MaterialBlock setBlastResistance(float resistance) {
        return (MaterialBlock) super.setBlastResistance(resistance);
    }

    @Override
    public MaterialBlock setHardness(float hardness) {
        return (MaterialBlock) super.setHardness(hardness);
    }

    @Override
    public MaterialBlock setUnbreakable() {
        return (MaterialBlock) super.setUnbreakable();
    }

    @Override
    public MaterialBlock setTicksRandomly(boolean ticksRandomly) {
        return (MaterialBlock) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public MaterialBlock setName(String string) {
        return (MaterialBlock) super.setTranslationKey(string);
    }

    @Override
    public MaterialBlock disableStat() {
        return (MaterialBlock) super.disableStat();
    }
}
