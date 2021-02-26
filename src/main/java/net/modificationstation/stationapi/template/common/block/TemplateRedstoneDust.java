package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplateRedstoneDust extends net.minecraft.block.RedstoneDust implements IBlockTemplate<TemplateRedstoneDust> {

    public TemplateRedstoneDust(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateRedstoneDust(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateRedstoneDust disableNotifyOnMetaDataChange() {
        return (TemplateRedstoneDust) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateRedstoneDust setSounds(BlockSounds sounds) {
        return (TemplateRedstoneDust) super.setSounds(sounds);
    }

    @Override
    public TemplateRedstoneDust setLightOpacity(int i) {
        return (TemplateRedstoneDust) super.setLightOpacity(i);
    }

    @Override
    public TemplateRedstoneDust setLightEmittance(float f) {
        return (TemplateRedstoneDust) super.setLightEmittance(f);
    }

    @Override
    public TemplateRedstoneDust setBlastResistance(float resistance) {
        return (TemplateRedstoneDust) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateRedstoneDust setHardness(float hardness) {
        return (TemplateRedstoneDust) super.setHardness(hardness);
    }

    @Override
    public TemplateRedstoneDust setUnbreakable() {
        return (TemplateRedstoneDust) super.setUnbreakable();
    }

    @Override
    public TemplateRedstoneDust setTicksRandomly(boolean ticksRandomly) {
        return (TemplateRedstoneDust) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateRedstoneDust setTranslationKey(String string) {
        return (TemplateRedstoneDust) super.setTranslationKey(string);
    }

    @Override
    public TemplateRedstoneDust disableStat() {
        return (TemplateRedstoneDust) super.disableStat();
    }
}
