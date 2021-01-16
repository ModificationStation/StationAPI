package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class SugarCane extends net.minecraft.block.SugarCane {
    
    public SugarCane(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public SugarCane(int i, int j) {
        super(i, j);
    }

    @Override
    public SugarCane disableNotifyOnMetaDataChange() {
        return (SugarCane) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public SugarCane sounds(BlockSounds sounds) {
        return (SugarCane) super.sounds(sounds);
    }

    @Override
    public SugarCane setLightOpacity(int i) {
        return (SugarCane) super.setLightOpacity(i);
    }

    @Override
    public SugarCane setLightEmittance(float f) {
        return (SugarCane) super.setLightEmittance(f);
    }

    @Override
    public SugarCane setBlastResistance(float resistance) {
        return (SugarCane) super.setBlastResistance(resistance);
    }

    @Override
    public SugarCane setHardness(float hardness) {
        return (SugarCane) super.setHardness(hardness);
    }

    @Override
    public SugarCane setUnbreakable() {
        return (SugarCane) super.setUnbreakable();
    }

    @Override
    public SugarCane setTicksRandomly(boolean ticksRandomly) {
        return (SugarCane) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public SugarCane setName(String string) {
        return (SugarCane) super.setName(string);
    }

    @Override
    public SugarCane disableStat() {
        return (SugarCane) super.disableStat();
    }
}
