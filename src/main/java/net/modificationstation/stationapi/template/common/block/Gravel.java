package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Gravel extends net.minecraft.block.Gravel {
    
    public Gravel(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Gravel(int i, int j) {
        super(i, j);
    }

    @Override
    public Gravel disableNotifyOnMetaDataChange() {
        return (Gravel) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Gravel sounds(BlockSounds sounds) {
        return (Gravel) super.sounds(sounds);
    }

    @Override
    public Gravel setLightOpacity(int i) {
        return (Gravel) super.setLightOpacity(i);
    }

    @Override
    public Gravel setLightEmittance(float f) {
        return (Gravel) super.setLightEmittance(f);
    }

    @Override
    public Gravel setBlastResistance(float resistance) {
        return (Gravel) super.setBlastResistance(resistance);
    }

    @Override
    public Gravel setHardness(float hardness) {
        return (Gravel) super.setHardness(hardness);
    }

    @Override
    public Gravel setUnbreakable() {
        return (Gravel) super.setUnbreakable();
    }

    @Override
    public Gravel setTicksRandomly(boolean ticksRandomly) {
        return (Gravel) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Gravel setName(String string) {
        return (Gravel) super.setName(string);
    }

    @Override
    public Gravel disableStat() {
        return (Gravel) super.disableStat();
    }
}
