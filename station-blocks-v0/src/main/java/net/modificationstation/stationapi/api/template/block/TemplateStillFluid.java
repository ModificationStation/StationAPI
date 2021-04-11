package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStillFluid extends net.minecraft.block.StillFluid implements IBlockTemplate<TemplateStillFluid> {
    
    public TemplateStillFluid(Identifier identifier, Material arg) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), arg);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateStillFluid(int i, Material arg) {
        super(i, arg);
    }

    @Override
    public TemplateStillFluid disableNotifyOnMetaDataChange() {
        return (TemplateStillFluid) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateStillFluid setSounds(BlockSounds sounds) {
        return (TemplateStillFluid) super.setSounds(sounds);
    }

    @Override
    public TemplateStillFluid setLightOpacity(int i) {
        return (TemplateStillFluid) super.setLightOpacity(i);
    }

    @Override
    public TemplateStillFluid setLightEmittance(float f) {
        return (TemplateStillFluid) super.setLightEmittance(f);
    }

    @Override
    public TemplateStillFluid setBlastResistance(float resistance) {
        return (TemplateStillFluid) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateStillFluid setHardness(float hardness) {
        return (TemplateStillFluid) super.setHardness(hardness);
    }

    @Override
    public TemplateStillFluid setUnbreakable() {
        return (TemplateStillFluid) super.setUnbreakable();
    }

    @Override
    public TemplateStillFluid setTicksRandomly(boolean ticksRandomly) {
        return (TemplateStillFluid) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateStillFluid setTranslationKey(String string) {
        return (TemplateStillFluid) super.setTranslationKey(string);
    }

    @Override
    public TemplateStillFluid disableStat() {
        return (TemplateStillFluid) super.disableStat();
    }
}
