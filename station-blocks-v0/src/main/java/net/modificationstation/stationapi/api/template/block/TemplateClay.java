package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateClay extends net.minecraft.block.Clay implements IBlockTemplate<TemplateClay> {

    public TemplateClay(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), texture);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateClay(int id, int texture) {
        super(id, texture);
    }

    @Override
    public TemplateClay disableNotifyOnMetaDataChange() {
        return (TemplateClay) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateClay setSounds(BlockSounds sounds) {
        return (TemplateClay) super.setSounds(sounds);
    }

    @Override
    public TemplateClay setLightOpacity(int i) {
        return (TemplateClay) super.setLightOpacity(i);
    }

    @Override
    public TemplateClay setLightEmittance(float f) {
        return (TemplateClay) super.setLightEmittance(f);
    }

    @Override
    public TemplateClay setBlastResistance(float resistance) {
        return (TemplateClay) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateClay setHardness(float hardness) {
        return (TemplateClay) super.setHardness(hardness);
    }

    @Override
    public TemplateClay setUnbreakable() {
        return (TemplateClay) super.setUnbreakable();
    }

    @Override
    public TemplateClay setTicksRandomly(boolean ticksRandomly) {
        return (TemplateClay) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateClay setTranslationKey(String string) {
        return (TemplateClay) super.setTranslationKey(string);
    }

    @Override
    public TemplateClay disableStat() {
        return (TemplateClay) super.disableStat();
    }
}
