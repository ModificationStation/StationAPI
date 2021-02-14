package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class RedstoneRepeater extends net.minecraft.block.RedstoneRepeater {
    
    public RedstoneRepeater(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public RedstoneRepeater(int i, boolean flag) {
        super(i, flag);
    }

    @Override
    public RedstoneRepeater disableNotifyOnMetaDataChange() {
        return (RedstoneRepeater) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public RedstoneRepeater setSounds(BlockSounds sounds) {
        return (RedstoneRepeater) super.setSounds(sounds);
    }

    @Override
    public RedstoneRepeater setLightOpacity(int i) {
        return (RedstoneRepeater) super.setLightOpacity(i);
    }

    @Override
    public RedstoneRepeater setLightEmittance(float f) {
        return (RedstoneRepeater) super.setLightEmittance(f);
    }

    @Override
    public RedstoneRepeater setBlastResistance(float resistance) {
        return (RedstoneRepeater) super.setBlastResistance(resistance);
    }

    @Override
    public RedstoneRepeater setHardness(float hardness) {
        return (RedstoneRepeater) super.setHardness(hardness);
    }

    @Override
    public RedstoneRepeater setUnbreakable() {
        return (RedstoneRepeater) super.setUnbreakable();
    }

    @Override
    public RedstoneRepeater setTicksRandomly(boolean ticksRandomly) {
        return (RedstoneRepeater) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public RedstoneRepeater setTranslationKey(String string) {
        return (RedstoneRepeater) super.setTranslationKey(string);
    }

    @Override
    public RedstoneRepeater disableStat() {
        return (RedstoneRepeater) super.disableStat();
    }
}
