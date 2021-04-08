package net.modificationstation.stationapi.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFluid extends net.minecraft.block.Fluid implements IBlockTemplate<TemplateFluid> {

    public TemplateFluid(Identifier identifier, Material arg) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), arg);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateFluid(int i, Material arg) {
        super(i, arg);
    }

    @Override
    public TemplateFluid disableNotifyOnMetaDataChange() {
        return (TemplateFluid) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateFluid setSounds(BlockSounds sounds) {
        return (TemplateFluid) super.setSounds(sounds);
    }

    @Override
    public TemplateFluid setLightOpacity(int i) {
        return (TemplateFluid) super.setLightOpacity(i);
    }

    @Override
    public TemplateFluid setLightEmittance(float f) {
        return (TemplateFluid) super.setLightEmittance(f);
    }

    @Override
    public TemplateFluid setBlastResistance(float resistance) {
        return (TemplateFluid) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateFluid setHardness(float hardness) {
        return (TemplateFluid) super.setHardness(hardness);
    }

    @Override
    public TemplateFluid setUnbreakable() {
        return (TemplateFluid) super.setUnbreakable();
    }

    @Override
    public TemplateFluid setTicksRandomly(boolean ticksRandomly) {
        return (TemplateFluid) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateFluid setTranslationKey(String string) {
        return (TemplateFluid) super.setTranslationKey(string);
    }

    @Override
    public TemplateFluid disableStat() {
        return (TemplateFluid) super.disableStat();
    }
}
