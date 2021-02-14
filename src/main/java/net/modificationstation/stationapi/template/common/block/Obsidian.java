package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Obsidian extends net.minecraft.block.Obsidian {
    
    public Obsidian(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Obsidian(int i, int j) {
        super(i, j);
    }

    @Override
    public Obsidian disableNotifyOnMetaDataChange() {
        return (Obsidian) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Obsidian setSounds(BlockSounds sounds) {
        return (Obsidian) super.setSounds(sounds);
    }

    @Override
    public Obsidian setLightOpacity(int i) {
        return (Obsidian) super.setLightOpacity(i);
    }

    @Override
    public Obsidian setLightEmittance(float f) {
        return (Obsidian) super.setLightEmittance(f);
    }

    @Override
    public Obsidian setBlastResistance(float resistance) {
        return (Obsidian) super.setBlastResistance(resistance);
    }

    @Override
    public Obsidian setHardness(float hardness) {
        return (Obsidian) super.setHardness(hardness);
    }

    @Override
    public Obsidian setUnbreakable() {
        return (Obsidian) super.setUnbreakable();
    }

    @Override
    public Obsidian setTicksRandomly(boolean ticksRandomly) {
        return (Obsidian) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Obsidian setTranslationKey(String string) {
        return (Obsidian) super.setTranslationKey(string);
    }

    @Override
    public Obsidian disableStat() {
        return (Obsidian) super.disableStat();
    }
}
