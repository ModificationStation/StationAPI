package net.modificationstation.stationapi.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateWorkbench extends net.minecraft.block.Workbench implements IBlockTemplate<TemplateWorkbench> {
    
    public TemplateWorkbench(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateWorkbench(int i) {
        super(i);
    }

    @Override
    public TemplateWorkbench disableNotifyOnMetaDataChange() {
        return (TemplateWorkbench) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateWorkbench setSounds(BlockSounds sounds) {
        return (TemplateWorkbench) super.setSounds(sounds);
    }

    @Override
    public TemplateWorkbench setLightOpacity(int i) {
        return (TemplateWorkbench) super.setLightOpacity(i);
    }

    @Override
    public TemplateWorkbench setLightEmittance(float f) {
        return (TemplateWorkbench) super.setLightEmittance(f);
    }

    @Override
    public TemplateWorkbench setBlastResistance(float resistance) {
        return (TemplateWorkbench) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateWorkbench setHardness(float hardness) {
        return (TemplateWorkbench) super.setHardness(hardness);
    }

    @Override
    public TemplateWorkbench setUnbreakable() {
        return (TemplateWorkbench) super.setUnbreakable();
    }

    @Override
    public TemplateWorkbench setTicksRandomly(boolean ticksRandomly) {
        return (TemplateWorkbench) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateWorkbench setTranslationKey(String string) {
        return (TemplateWorkbench) super.setTranslationKey(string);
    }

    @Override
    public TemplateWorkbench disableStat() {
        return (TemplateWorkbench) super.disableStat();
    }
}
