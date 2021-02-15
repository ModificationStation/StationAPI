package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class DetectorRail extends net.minecraft.block.DetectorRail implements IBlockTemplate<DetectorRail> {
    
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
    public DetectorRail setSounds(BlockSounds sounds) {
        return (DetectorRail) super.setSounds(sounds);
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

    @Override
    public DetectorRail setTicksRandomly(boolean ticksRandomly) {
        return (DetectorRail) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public DetectorRail setTranslationKey(String string) {
        return (DetectorRail) super.setTranslationKey(string);
    }

    @Override
    public DetectorRail disableStat() {
        return (DetectorRail) super.disableStat();
    }
}
