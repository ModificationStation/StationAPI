package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplateRail extends net.minecraft.block.Rail implements IBlockTemplate<TemplateRail> {

    public TemplateRail(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateRail(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public TemplateRail disableNotifyOnMetaDataChange() {
        return (TemplateRail) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateRail setSounds(BlockSounds sounds) {
        return (TemplateRail) super.setSounds(sounds);
    }

    @Override
    public TemplateRail setLightOpacity(int i) {
        return (TemplateRail) super.setLightOpacity(i);
    }

    @Override
    public TemplateRail setLightEmittance(float f) {
        return (TemplateRail) super.setLightEmittance(f);
    }

    @Override
    public TemplateRail setBlastResistance(float resistance) {
        return (TemplateRail) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateRail setHardness(float hardness) {
        return (TemplateRail) super.setHardness(hardness);
    }

    @Override
    public TemplateRail setUnbreakable() {
        return (TemplateRail) super.setUnbreakable();
    }

    @Override
    public TemplateRail setTicksRandomly(boolean ticksRandomly) {
        return (TemplateRail) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateRail setTranslationKey(String string) {
        return (TemplateRail) super.setTranslationKey(string);
    }

    @Override
    public TemplateRail disableStat() {
        return (TemplateRail) super.disableStat();
    }
}
