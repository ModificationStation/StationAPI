package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateRedstoneRepeater extends net.minecraft.block.RedstoneRepeater implements IBlockTemplate<TemplateRedstoneRepeater> {
    
    public TemplateRedstoneRepeater(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateRedstoneRepeater(int i, boolean flag) {
        super(i, flag);
    }

    @Override
    public TemplateRedstoneRepeater disableNotifyOnMetaDataChange() {
        return (TemplateRedstoneRepeater) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateRedstoneRepeater setSounds(BlockSounds sounds) {
        return (TemplateRedstoneRepeater) super.setSounds(sounds);
    }

    @Override
    public TemplateRedstoneRepeater setLightOpacity(int i) {
        return (TemplateRedstoneRepeater) super.setLightOpacity(i);
    }

    @Override
    public TemplateRedstoneRepeater setLightEmittance(float f) {
        return (TemplateRedstoneRepeater) super.setLightEmittance(f);
    }

    @Override
    public TemplateRedstoneRepeater setBlastResistance(float resistance) {
        return (TemplateRedstoneRepeater) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateRedstoneRepeater setHardness(float hardness) {
        return (TemplateRedstoneRepeater) super.setHardness(hardness);
    }

    @Override
    public TemplateRedstoneRepeater setUnbreakable() {
        return (TemplateRedstoneRepeater) super.setUnbreakable();
    }

    @Override
    public TemplateRedstoneRepeater setTicksRandomly(boolean ticksRandomly) {
        return (TemplateRedstoneRepeater) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateRedstoneRepeater setTranslationKey(String string) {
        return (TemplateRedstoneRepeater) super.setTranslationKey(string);
    }

    @Override
    public TemplateRedstoneRepeater disableStat() {
        return (TemplateRedstoneRepeater) super.disableStat();
    }
}
