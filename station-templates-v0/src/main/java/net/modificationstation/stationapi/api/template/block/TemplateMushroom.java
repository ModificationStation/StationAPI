package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMushroom extends net.minecraft.block.Mushroom implements BlockTemplate<TemplateMushroom> {

    public TemplateMushroom(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateMushroom(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateMushroom disableNotifyOnMetaDataChange() {
        return (TemplateMushroom) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateMushroom setSounds(BlockSounds sounds) {
        return (TemplateMushroom) super.setSounds(sounds);
    }

    @Override
    public TemplateMushroom setLightOpacity(int i) {
        return (TemplateMushroom) super.setLightOpacity(i);
    }

    @Override
    public TemplateMushroom setLightEmittance(float f) {
        return (TemplateMushroom) super.setLightEmittance(f);
    }

    @Override
    public TemplateMushroom setBlastResistance(float resistance) {
        return (TemplateMushroom) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateMushroom setHardness(float hardness) {
        return (TemplateMushroom) super.setHardness(hardness);
    }

    @Override
    public TemplateMushroom setUnbreakable() {
        return (TemplateMushroom) super.setUnbreakable();
    }

    @Override
    public TemplateMushroom setTicksRandomly(boolean ticksRandomly) {
        return (TemplateMushroom) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateMushroom setTranslationKey(String string) {
        return (TemplateMushroom) super.setTranslationKey(string);
    }

    @Override
    public TemplateMushroom disableStat() {
        return (TemplateMushroom) super.disableStat();
    }
}
