package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateIce extends net.minecraft.block.Ice implements BlockTemplate<TemplateIce> {

    public TemplateIce(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateIce(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateIce disableNotifyOnMetaDataChange() {
        return (TemplateIce) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateIce setSounds(BlockSounds sounds) {
        return (TemplateIce) super.setSounds(sounds);
    }

    @Override
    public TemplateIce setLightOpacity(int i) {
        return (TemplateIce) super.setLightOpacity(i);
    }

    @Override
    public TemplateIce setLightEmittance(float f) {
        return (TemplateIce) super.setLightEmittance(f);
    }

    @Override
    public TemplateIce setBlastResistance(float resistance) {
        return (TemplateIce) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateIce setHardness(float hardness) {
        return (TemplateIce) super.setHardness(hardness);
    }

    @Override
    public TemplateIce setUnbreakable() {
        return (TemplateIce) super.setUnbreakable();
    }

    @Override
    public TemplateIce setTicksRandomly(boolean ticksRandomly) {
        return (TemplateIce) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateIce setTranslationKey(String string) {
        return (TemplateIce) super.setTranslationKey(string);
    }

    @Override
    public TemplateIce disableStat() {
        return (TemplateIce) super.disableStat();
    }

    @Override
    public Atlas getAtlas() {
        return BlockTemplate.super.getAtlas();
    }
}
