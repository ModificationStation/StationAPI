package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSand extends net.minecraft.block.Sand implements BlockTemplate<TemplateSand> {
    
    public TemplateSand(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateSand(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateSand disableNotifyOnMetaDataChange() {
        return (TemplateSand) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateSand setSounds(BlockSounds sounds) {
        return (TemplateSand) super.setSounds(sounds);
    }

    @Override
    public TemplateSand setLightOpacity(int i) {
        return (TemplateSand) super.setLightOpacity(i);
    }

    @Override
    public TemplateSand setLightEmittance(float f) {
        return (TemplateSand) super.setLightEmittance(f);
    }

    @Override
    public TemplateSand setBlastResistance(float resistance) {
        return (TemplateSand) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateSand setHardness(float hardness) {
        return (TemplateSand) super.setHardness(hardness);
    }

    @Override
    public TemplateSand setUnbreakable() {
        return (TemplateSand) super.setUnbreakable();
    }

    @Override
    public TemplateSand setTicksRandomly(boolean ticksRandomly) {
        return (TemplateSand) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateSand setTranslationKey(String string) {
        return (TemplateSand) super.setTranslationKey(string);
    }

    @Override
    public TemplateSand disableStat() {
        return (TemplateSand) super.disableStat();
    }
}
