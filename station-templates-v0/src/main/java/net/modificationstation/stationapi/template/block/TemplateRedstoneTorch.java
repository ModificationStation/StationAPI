package net.modificationstation.stationapi.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneTorch extends net.minecraft.block.RedstoneTorch implements IBlockTemplate<TemplateRedstoneTorch> {
    
    public TemplateRedstoneTorch(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateRedstoneTorch(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public TemplateRedstoneTorch disableNotifyOnMetaDataChange() {
        return (TemplateRedstoneTorch) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateRedstoneTorch setSounds(BlockSounds sounds) {
        return (TemplateRedstoneTorch) super.setSounds(sounds);
    }

    @Override
    public TemplateRedstoneTorch setLightOpacity(int i) {
        return (TemplateRedstoneTorch) super.setLightOpacity(i);
    }

    @Override
    public TemplateRedstoneTorch setLightEmittance(float f) {
        return (TemplateRedstoneTorch) super.setLightEmittance(f);
    }

    @Override
    public TemplateRedstoneTorch setBlastResistance(float resistance) {
        return (TemplateRedstoneTorch) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateRedstoneTorch setHardness(float hardness) {
        return (TemplateRedstoneTorch) super.setHardness(hardness);
    }

    @Override
    public TemplateRedstoneTorch setUnbreakable() {
        return (TemplateRedstoneTorch) super.setUnbreakable();
    }

    @Override
    public TemplateRedstoneTorch setTicksRandomly(boolean ticksRandomly) {
        return (TemplateRedstoneTorch) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateRedstoneTorch setTranslationKey(String string) {
        return (TemplateRedstoneTorch) super.setTranslationKey(string);
    }

    @Override
    public TemplateRedstoneTorch disableStat() {
        return (TemplateRedstoneTorch) super.disableStat();
    }
}
