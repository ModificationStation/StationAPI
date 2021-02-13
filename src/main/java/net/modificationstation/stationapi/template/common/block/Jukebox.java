package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Jukebox extends net.minecraft.block.Jukebox {
    
    public Jukebox(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Jukebox(int i, int j) {
        super(i, j);
    }

    @Override
    public Jukebox disableNotifyOnMetaDataChange() {
        return (Jukebox) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Jukebox sounds(BlockSounds sounds) {
        return (Jukebox) super.setSounds(sounds);
    }

    @Override
    public Jukebox setLightOpacity(int i) {
        return (Jukebox) super.setLightOpacity(i);
    }

    @Override
    public Jukebox setLightEmittance(float f) {
        return (Jukebox) super.setLightEmittance(f);
    }

    @Override
    public Jukebox setBlastResistance(float resistance) {
        return (Jukebox) super.setBlastResistance(resistance);
    }

    @Override
    public Jukebox setHardness(float hardness) {
        return (Jukebox) super.setHardness(hardness);
    }

    @Override
    public Jukebox setUnbreakable() {
        return (Jukebox) super.setUnbreakable();
    }

    @Override
    public Jukebox setTicksRandomly(boolean ticksRandomly) {
        return (Jukebox) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Jukebox setName(String string) {
        return (Jukebox) super.setTranslationKey(string);
    }

    @Override
    public Jukebox disableStat() {
        return (Jukebox) super.disableStat();
    }
}
