package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateLog extends net.minecraft.block.Log implements BlockTemplate<TemplateLog> {
    
    public TemplateLog(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerialID());
        BlockRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateLog(int i) {
        super(i);
    }

    @Override
    public TemplateLog disableNotifyOnMetaDataChange() {
        return (TemplateLog) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateLog setSounds(BlockSounds sounds) {
        return (TemplateLog) super.setSounds(sounds);
    }

    @Override
    public TemplateLog setLightOpacity(int i) {
        return (TemplateLog) super.setLightOpacity(i);
    }

    @Override
    public TemplateLog setLightEmittance(float f) {
        return (TemplateLog) super.setLightEmittance(f);
    }

    @Override
    public TemplateLog setBlastResistance(float resistance) {
        return (TemplateLog) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateLog setHardness(float hardness) {
        return (TemplateLog) super.setHardness(hardness);
    }

    @Override
    public TemplateLog setUnbreakable() {
        return (TemplateLog) super.setUnbreakable();
    }

    @Override
    public TemplateLog setTicksRandomly(boolean ticksRandomly) {
        return (TemplateLog) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateLog setTranslationKey(String string) {
        return (TemplateLog) super.setTranslationKey(string);
    }

    @Override
    public TemplateLog disableStat() {
        return (TemplateLog) super.disableStat();
    }

    @Override
    public Atlas getAtlas() {
        return BlockTemplate.super.getAtlas();
    }
}
