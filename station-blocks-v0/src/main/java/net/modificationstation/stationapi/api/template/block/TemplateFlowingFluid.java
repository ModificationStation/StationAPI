package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFlowingFluid extends net.minecraft.block.FlowingFluid implements IBlockTemplate<TemplateFlowingFluid> {

    public TemplateFlowingFluid(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), material);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateFlowingFluid(int i, Material arg) {
        super(i, arg);
    }

    @Override
    public TemplateFlowingFluid disableNotifyOnMetaDataChange() {
        return (TemplateFlowingFluid) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateFlowingFluid setSounds(BlockSounds sounds) {
        return (TemplateFlowingFluid) super.setSounds(sounds);
    }

    @Override
    public TemplateFlowingFluid setLightOpacity(int i) {
        return (TemplateFlowingFluid) super.setLightOpacity(i);
    }

    @Override
    public TemplateFlowingFluid setLightEmittance(float f) {
        return (TemplateFlowingFluid) super.setLightEmittance(f);
    }

    @Override
    public TemplateFlowingFluid setBlastResistance(float resistance) {
        return (TemplateFlowingFluid) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateFlowingFluid setHardness(float hardness) {
        return (TemplateFlowingFluid) super.setHardness(hardness);
    }

    @Override
    public TemplateFlowingFluid setUnbreakable() {
        return (TemplateFlowingFluid) super.setUnbreakable();
    }

    @Override
    public TemplateFlowingFluid setTicksRandomly(boolean ticksRandomly) {
        return (TemplateFlowingFluid) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateFlowingFluid setTranslationKey(String string) {
        return (TemplateFlowingFluid) super.setTranslationKey(string);
    }

    @Override
    public TemplateFlowingFluid disableStat() {
        return (TemplateFlowingFluid) super.disableStat();
    }
}
