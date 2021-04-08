package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTnt extends net.minecraft.block.Tnt implements IBlockTemplate<TemplateTnt> {
    
    public TemplateTnt(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateTnt(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateTnt disableNotifyOnMetaDataChange() {
        return (TemplateTnt) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateTnt setSounds(BlockSounds sounds) {
        return (TemplateTnt) super.setSounds(sounds);
    }

    @Override
    public TemplateTnt setLightOpacity(int i) {
        return (TemplateTnt) super.setLightOpacity(i);
    }

    @Override
    public TemplateTnt setLightEmittance(float f) {
        return (TemplateTnt) super.setLightEmittance(f);
    }

    @Override
    public TemplateTnt setBlastResistance(float resistance) {
        return (TemplateTnt) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateTnt setHardness(float hardness) {
        return (TemplateTnt) super.setHardness(hardness);
    }

    @Override
    public TemplateTnt setUnbreakable() {
        return (TemplateTnt) super.setUnbreakable();
    }

    @Override
    public TemplateTnt setTicksRandomly(boolean ticksRandomly) {
        return (TemplateTnt) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateTnt setTranslationKey(String string) {
        return (TemplateTnt) super.setTranslationKey(string);
    }

    @Override
    public TemplateTnt disableStat() {
        return (TemplateTnt) super.disableStat();
    }
}
