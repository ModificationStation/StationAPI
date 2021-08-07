package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFurnace extends net.minecraft.block.Furnace implements BlockTemplate<TemplateFurnace> {

    public TemplateFurnace(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), flag);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateFurnace(int i, boolean flag) {
        super(i, flag);
    }

    @Override
    public TemplateFurnace disableNotifyOnMetaDataChange() {
        return (TemplateFurnace) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateFurnace setSounds(BlockSounds sounds) {
        return (TemplateFurnace) super.setSounds(sounds);
    }

    @Override
    public TemplateFurnace setLightOpacity(int i) {
        return (TemplateFurnace) super.setLightOpacity(i);
    }

    @Override
    public TemplateFurnace setLightEmittance(float f) {
        return (TemplateFurnace) super.setLightEmittance(f);
    }

    @Override
    public TemplateFurnace setBlastResistance(float resistance) {
        return (TemplateFurnace) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateFurnace setHardness(float hardness) {
        return (TemplateFurnace) super.setHardness(hardness);
    }

    @Override
    public TemplateFurnace setUnbreakable() {
        return (TemplateFurnace) super.setUnbreakable();
    }

    @Override
    public TemplateFurnace setTicksRandomly(boolean ticksRandomly) {
        return (TemplateFurnace) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateFurnace setTranslationKey(String string) {
        return (TemplateFurnace) super.setTranslationKey(string);
    }

    @Override
    public TemplateFurnace disableStat() {
        return (TemplateFurnace) super.disableStat();
    }

    @Override
    public Atlas getAtlas() {
        return BlockTemplate.super.getAtlas();
    }
}
