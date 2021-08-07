package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBed extends net.minecraft.block.Bed implements BlockTemplate<TemplateBed> {

    public TemplateBed(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerialID());
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateBed(int id) {
        super(id);
    }

    @Override
    public TemplateBed disableNotifyOnMetaDataChange() {
        return (TemplateBed) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateBed setSounds(BlockSounds sounds) {
        return (TemplateBed) super.setSounds(sounds);
    }

    @Override
    public TemplateBed setLightOpacity(int i) {
        return (TemplateBed) super.setLightOpacity(i);
    }

    @Override
    public TemplateBed setLightEmittance(float f) {
        return (TemplateBed) super.setLightEmittance(f);
    }

    @Override
    public TemplateBed setBlastResistance(float resistance) {
        return (TemplateBed) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateBed setHardness(float hardness) {
        return (TemplateBed) super.setHardness(hardness);
    }

    @Override
    public TemplateBed setUnbreakable() {
        return (TemplateBed) super.setUnbreakable();
    }

    @Override
    public TemplateBed setTicksRandomly(boolean ticksRandomly) {
        return (TemplateBed) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateBed setTranslationKey(String string) {
        return (TemplateBed) super.setTranslationKey(string);
    }

    @Override
    public TemplateBed disableStat() {
        return (TemplateBed) super.disableStat();
    }

    @Override
    public Atlas getAtlas() {
        return BlockTemplate.super.getAtlas();
    }
}
