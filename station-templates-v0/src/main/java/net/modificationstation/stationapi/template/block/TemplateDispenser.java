package net.modificationstation.stationapi.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDispenser extends net.minecraft.block.Dispenser implements IBlockTemplate<TemplateDispenser> {

    public TemplateDispenser(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateDispenser(int id) {
        super(id);
    }

    @Override
    public TemplateDispenser disableNotifyOnMetaDataChange() {
        return (TemplateDispenser) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateDispenser setSounds(BlockSounds sounds) {
        return (TemplateDispenser) super.setSounds(sounds);
    }

    @Override
    public TemplateDispenser setLightOpacity(int i) {
        return (TemplateDispenser) super.setLightOpacity(i);
    }

    @Override
    public TemplateDispenser setLightEmittance(float f) {
        return (TemplateDispenser) super.setLightEmittance(f);
    }

    @Override
    public TemplateDispenser setBlastResistance(float resistance) {
        return (TemplateDispenser) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateDispenser setHardness(float hardness) {
        return (TemplateDispenser) super.setHardness(hardness);
    }

    @Override
    public TemplateDispenser setUnbreakable() {
        return (TemplateDispenser) super.setUnbreakable();
    }

    @Override
    public TemplateDispenser setTicksRandomly(boolean ticksRandomly) {
        return (TemplateDispenser) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateDispenser setTranslationKey(String string) {
        return (TemplateDispenser) super.setTranslationKey(string);
    }

    @Override
    public TemplateDispenser disableStat() {
        return (TemplateDispenser) super.disableStat();
    }
}
