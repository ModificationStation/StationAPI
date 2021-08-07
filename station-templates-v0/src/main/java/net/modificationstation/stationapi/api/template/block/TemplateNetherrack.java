package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateNetherrack extends net.minecraft.block.Netherrack implements BlockTemplate<TemplateNetherrack> {

    public TemplateNetherrack(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateNetherrack(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateNetherrack disableNotifyOnMetaDataChange() {
        return (TemplateNetherrack) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateNetherrack setSounds(BlockSounds sounds) {
        return (TemplateNetherrack) super.setSounds(sounds);
    }

    @Override
    public TemplateNetherrack setLightOpacity(int i) {
        return (TemplateNetherrack) super.setLightOpacity(i);
    }

    @Override
    public TemplateNetherrack setLightEmittance(float f) {
        return (TemplateNetherrack) super.setLightEmittance(f);
    }

    @Override
    public TemplateNetherrack setBlastResistance(float resistance) {
        return (TemplateNetherrack) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateNetherrack setHardness(float hardness) {
        return (TemplateNetherrack) super.setHardness(hardness);
    }

    @Override
    public TemplateNetherrack setUnbreakable() {
        return (TemplateNetherrack) super.setUnbreakable();
    }

    @Override
    public TemplateNetherrack setTicksRandomly(boolean ticksRandomly) {
        return (TemplateNetherrack) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateNetherrack setTranslationKey(String string) {
        return (TemplateNetherrack) super.setTranslationKey(string);
    }

    @Override
    public TemplateNetherrack disableStat() {
        return (TemplateNetherrack) super.disableStat();
    }

    @Override
    public Atlas getAtlas() {
        return BlockTemplate.super.getAtlas();
    }
}
