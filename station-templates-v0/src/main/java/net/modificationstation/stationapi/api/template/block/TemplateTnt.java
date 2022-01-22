package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTnt extends net.minecraft.block.Tnt implements BlockTemplate<TemplateTnt> {
    
    public TemplateTnt(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateTnt(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateTnt disableNotifyOnMetaDataChange() {
        return (TemplateTnt) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateTnt setSounds(BlockSounds sounds) {
        return (TemplateTnt) super.setSounds(sounds);
    }

    @Override
    public TemplateTnt setLightOpacity(int i) {
        return (TemplateTnt) super.setLightOpacity(i);
    }

    @Override
    public TemplateTnt setLightEmittance(float f) {
        return (TemplateTnt) super.setLightEmittance(f);
    }

    @Override
    public TemplateTnt setBlastResistance(float resistance) {
        return (TemplateTnt) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateTnt setHardness(float hardness) {
        return (TemplateTnt) super.setHardness(hardness);
    }

    @Override
    public TemplateTnt setUnbreakable() {
        return (TemplateTnt) super.setUnbreakable();
    }

    @Override
    public TemplateTnt setTicksRandomly(boolean ticksRandomly) {
        return (TemplateTnt) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateTnt setTranslationKey(String string) {
        return (TemplateTnt) super.setTranslationKey(string);
    }

    @Override
    public TemplateTnt disableStat() {
        return (TemplateTnt) super.disableStat();
    }
}
