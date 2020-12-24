package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Noteblock extends net.minecraft.block.Noteblock {
    
    public Noteblock(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Noteblock(int i) {
        super(i);
    }

    @Override
    public Noteblock disableNotifyOnMetaDataChange() {
        return (Noteblock) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Noteblock sounds(BlockSounds sounds) {
        return (Noteblock) super.sounds(sounds);
    }

    @Override
    public Noteblock setLightOpacity(int i) {
        return (Noteblock) super.setLightOpacity(i);
    }

    @Override
    public Noteblock setLightEmittance(float f) {
        return (Noteblock) super.setLightEmittance(f);
    }

    @Override
    public Noteblock setBlastResistance(float resistance) {
        return (Noteblock) super.setBlastResistance(resistance);
    }

    @Override
    public Noteblock setHardness(float hardness) {
        return (Noteblock) super.setHardness(hardness);
    }

    @Override
    public Noteblock setUnbreakable() {
        return (Noteblock) super.setUnbreakable();
    }
}