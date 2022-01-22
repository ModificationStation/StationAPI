package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDetectorRail extends net.minecraft.block.DetectorRail implements BlockTemplate<TemplateDetectorRail> {
    
    public TemplateDetectorRail(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), texture);
        BlockRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateDetectorRail(int id, int texture) {
        super(id, texture);
    }

    @Override
    public TemplateDetectorRail disableNotifyOnMetaDataChange() {
        return (TemplateDetectorRail) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateDetectorRail setSounds(BlockSounds sounds) {
        return (TemplateDetectorRail) super.setSounds(sounds);
    }

    @Override
    public TemplateDetectorRail setLightOpacity(int i) {
        return (TemplateDetectorRail) super.setLightOpacity(i);
    }

    @Override
    public TemplateDetectorRail setLightEmittance(float f) {
        return (TemplateDetectorRail) super.setLightEmittance(f);
    }

    @Override
    public TemplateDetectorRail setBlastResistance(float resistance) {
        return (TemplateDetectorRail) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateDetectorRail setHardness(float hardness) {
        return (TemplateDetectorRail) super.setHardness(hardness);
    }

    @Override
    public TemplateDetectorRail setUnbreakable() {
        return (TemplateDetectorRail) super.setUnbreakable();
    }

    @Override
    public TemplateDetectorRail setTicksRandomly(boolean ticksRandomly) {
        return (TemplateDetectorRail) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateDetectorRail setTranslationKey(String string) {
        return (TemplateDetectorRail) super.setTranslationKey(string);
    }

    @Override
    public TemplateDetectorRail disableStat() {
        return (TemplateDetectorRail) super.disableStat();
    }
}
