package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class RedstoneOre extends net.minecraft.block.RedstoneOre {
    
    public RedstoneOre(Identifier identifier, int j, boolean isLit) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, isLit);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public RedstoneOre(int i, int j, boolean isLit) {
        super(i, j, isLit);
    }

    @Override
    public RedstoneOre disableNotifyOnMetaDataChange() {
        return (RedstoneOre) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public RedstoneOre sounds(BlockSounds sounds) {
        return (RedstoneOre) super.setSounds(sounds);
    }

    @Override
    public RedstoneOre setLightOpacity(int i) {
        return (RedstoneOre) super.setLightOpacity(i);
    }

    @Override
    public RedstoneOre setLightEmittance(float f) {
        return (RedstoneOre) super.setLightEmittance(f);
    }

    @Override
    public RedstoneOre setBlastResistance(float resistance) {
        return (RedstoneOre) super.setBlastResistance(resistance);
    }

    @Override
    public RedstoneOre setHardness(float hardness) {
        return (RedstoneOre) super.setHardness(hardness);
    }

    @Override
    public RedstoneOre setUnbreakable() {
        return (RedstoneOre) super.setUnbreakable();
    }

    @Override
    public RedstoneOre setTicksRandomly(boolean ticksRandomly) {
        return (RedstoneOre) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public RedstoneOre setName(String string) {
        return (RedstoneOre) super.setTranslationKey(string);
    }

    @Override
    public RedstoneOre disableStat() {
        return (RedstoneOre) super.disableStat();
    }
}
