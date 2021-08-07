package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTranslucentBlock extends net.minecraft.block.TranslucentBlock implements BlockTemplate<TemplateTranslucentBlock> {
    
    public TemplateTranslucentBlock(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j, arg, flag);
        BlockRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateTranslucentBlock(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }

    @Override
    public TemplateTranslucentBlock disableNotifyOnMetaDataChange() {
        return (TemplateTranslucentBlock) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateTranslucentBlock setSounds(BlockSounds sounds) {
        return (TemplateTranslucentBlock) super.setSounds(sounds);
    }

    @Override
    public TemplateTranslucentBlock setLightOpacity(int i) {
        return (TemplateTranslucentBlock) super.setLightOpacity(i);
    }

    @Override
    public TemplateTranslucentBlock setLightEmittance(float f) {
        return (TemplateTranslucentBlock) super.setLightEmittance(f);
    }

    @Override
    public TemplateTranslucentBlock setBlastResistance(float resistance) {
        return (TemplateTranslucentBlock) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateTranslucentBlock setHardness(float hardness) {
        return (TemplateTranslucentBlock) super.setHardness(hardness);
    }

    @Override
    public TemplateTranslucentBlock setUnbreakable() {
        return (TemplateTranslucentBlock) super.setUnbreakable();
    }

    @Override
    public TemplateTranslucentBlock setTicksRandomly(boolean ticksRandomly) {
        return (TemplateTranslucentBlock) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateTranslucentBlock setTranslationKey(String string) {
        return (TemplateTranslucentBlock) super.setTranslationKey(string);
    }

    @Override
    public TemplateTranslucentBlock disableStat() {
        return (TemplateTranslucentBlock) super.disableStat();
    }

    @Override
    public Atlas getAtlas() {
        return BlockTemplate.super.getAtlas();
    }
}
