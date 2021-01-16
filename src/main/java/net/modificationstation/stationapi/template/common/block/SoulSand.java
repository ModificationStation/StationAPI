package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class SoulSand extends net.minecraft.block.SoulSand {
    
    public SoulSand(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public SoulSand(int i, int j) {
        super(i, j);
    }

    @Override
    public SoulSand disableNotifyOnMetaDataChange() {
        return (SoulSand) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public SoulSand sounds(BlockSounds sounds) {
        return (SoulSand) super.sounds(sounds);
    }

    @Override
    public SoulSand setLightOpacity(int i) {
        return (SoulSand) super.setLightOpacity(i);
    }

    @Override
    public SoulSand setLightEmittance(float f) {
        return (SoulSand) super.setLightEmittance(f);
    }

    @Override
    public SoulSand setBlastResistance(float resistance) {
        return (SoulSand) super.setBlastResistance(resistance);
    }

    @Override
    public SoulSand setHardness(float hardness) {
        return (SoulSand) super.setHardness(hardness);
    }

    @Override
    public SoulSand setUnbreakable() {
        return (SoulSand) super.setUnbreakable();
    }

    @Override
    public SoulSand setTicksRandomly(boolean ticksRandomly) {
        return (SoulSand) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public SoulSand setName(String string) {
        return (SoulSand) super.setName(string);
    }

    @Override
    public SoulSand disableStat() {
        return (SoulSand) super.disableStat();
    }
}
