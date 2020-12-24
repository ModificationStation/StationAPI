package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

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
        return (Jukebox) super.sounds(sounds);
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
}
