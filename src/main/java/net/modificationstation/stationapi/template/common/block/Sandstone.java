package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Sandstone extends net.minecraft.block.Sandstone {
    
    public Sandstone(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Sandstone(int i) {
        super(i);
    }

    @Override
    public Sandstone disableNotifyOnMetaDataChange() {
        return (Sandstone) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Sandstone setSounds(BlockSounds sounds) {
        return (Sandstone) super.setSounds(sounds);
    }

    @Override
    public Sandstone setLightOpacity(int i) {
        return (Sandstone) super.setLightOpacity(i);
    }

    @Override
    public Sandstone setLightEmittance(float f) {
        return (Sandstone) super.setLightEmittance(f);
    }

    @Override
    public Sandstone setBlastResistance(float resistance) {
        return (Sandstone) super.setBlastResistance(resistance);
    }

    @Override
    public Sandstone setHardness(float hardness) {
        return (Sandstone) super.setHardness(hardness);
    }

    @Override
    public Sandstone setUnbreakable() {
        return (Sandstone) super.setUnbreakable();
    }

    @Override
    public Sandstone setTicksRandomly(boolean ticksRandomly) {
        return (Sandstone) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Sandstone setTranslationKey(String string) {
        return (Sandstone) super.setTranslationKey(string);
    }

    @Override
    public Sandstone disableStat() {
        return (Sandstone) super.disableStat();
    }
}
