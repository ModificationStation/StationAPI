package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class DetectorRail extends net.minecraft.block.DetectorRail {
    
    public DetectorRail(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public DetectorRail(int id, int texture) {
        super(id, texture);
    }

    @Override
    public DetectorRail disableNotifyOnMetaDataChange() {
        return (DetectorRail) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public DetectorRail sounds(BlockSounds sounds) {
        return (DetectorRail) super.sounds(sounds);
    }

    @Override
    public DetectorRail setLightOpacity(int i) {
        return (DetectorRail) super.setLightOpacity(i);
    }

    @Override
    public DetectorRail setLightEmittance(float f) {
        return (DetectorRail) super.setLightEmittance(f);
    }

    @Override
    public DetectorRail setBlastResistance(float resistance) {
        return (DetectorRail) super.setBlastResistance(resistance);
    }

    @Override
    public DetectorRail setHardness(float hardness) {
        return (DetectorRail) super.setHardness(hardness);
    }

    @Override
    public DetectorRail setUnbreakable() {
        return (DetectorRail) super.setUnbreakable();
    }
}
