package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

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
        return (Noteblock) super.setSounds(sounds);
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

    @Override
    public Noteblock setTicksRandomly(boolean ticksRandomly) {
        return (Noteblock) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Noteblock setName(String string) {
        return (Noteblock) super.setTranslationKey(string);
    }

    @Override
    public Noteblock disableStat() {
        return (Noteblock) super.disableStat();
    }
}
