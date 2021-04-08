package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateObsidian extends net.minecraft.block.Obsidian implements IBlockTemplate<TemplateObsidian> {
    
    public TemplateObsidian(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateObsidian(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateObsidian disableNotifyOnMetaDataChange() {
        return (TemplateObsidian) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateObsidian setSounds(BlockSounds sounds) {
        return (TemplateObsidian) super.setSounds(sounds);
    }

    @Override
    public TemplateObsidian setLightOpacity(int i) {
        return (TemplateObsidian) super.setLightOpacity(i);
    }

    @Override
    public TemplateObsidian setLightEmittance(float f) {
        return (TemplateObsidian) super.setLightEmittance(f);
    }

    @Override
    public TemplateObsidian setBlastResistance(float resistance) {
        return (TemplateObsidian) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateObsidian setHardness(float hardness) {
        return (TemplateObsidian) super.setHardness(hardness);
    }

    @Override
    public TemplateObsidian setUnbreakable() {
        return (TemplateObsidian) super.setUnbreakable();
    }

    @Override
    public TemplateObsidian setTicksRandomly(boolean ticksRandomly) {
        return (TemplateObsidian) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateObsidian setTranslationKey(String string) {
        return (TemplateObsidian) super.setTranslationKey(string);
    }

    @Override
    public TemplateObsidian disableStat() {
        return (TemplateObsidian) super.disableStat();
    }
}
