package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTorch extends net.minecraft.block.Torch implements IBlockTemplate<TemplateTorch> {
    
    public TemplateTorch(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateTorch(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateTorch disableNotifyOnMetaDataChange() {
        return (TemplateTorch) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateTorch setSounds(BlockSounds sounds) {
        return (TemplateTorch) super.setSounds(sounds);
    }

    @Override
    public TemplateTorch setLightOpacity(int i) {
        return (TemplateTorch) super.setLightOpacity(i);
    }

    @Override
    public TemplateTorch setLightEmittance(float f) {
        return (TemplateTorch) super.setLightEmittance(f);
    }

    @Override
    public TemplateTorch setBlastResistance(float resistance) {
        return (TemplateTorch) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateTorch setHardness(float hardness) {
        return (TemplateTorch) super.setHardness(hardness);
    }

    @Override
    public TemplateTorch setUnbreakable() {
        return (TemplateTorch) super.setUnbreakable();
    }

    @Override
    public TemplateTorch setTicksRandomly(boolean ticksRandomly) {
        return (TemplateTorch) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateTorch setTranslationKey(String string) {
        return (TemplateTorch) super.setTranslationKey(string);
    }

    @Override
    public TemplateTorch disableStat() {
        return (TemplateTorch) super.disableStat();
    }
}
