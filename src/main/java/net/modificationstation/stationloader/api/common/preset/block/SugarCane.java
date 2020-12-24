package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

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
}
