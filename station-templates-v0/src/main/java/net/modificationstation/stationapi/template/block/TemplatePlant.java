package net.modificationstation.stationapi.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePlant extends net.minecraft.block.Plant implements IBlockTemplate<TemplatePlant> {

    public TemplatePlant(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplatePlant(int id, int texture) {
        super(id, texture);
    }

    @Override
    public TemplatePlant disableNotifyOnMetaDataChange() {
        return (TemplatePlant) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplatePlant setSounds(BlockSounds sounds) {
        return (TemplatePlant) super.setSounds(sounds);
    }

    @Override
    public TemplatePlant setLightOpacity(int i) {
        return (TemplatePlant) super.setLightOpacity(i);
    }

    @Override
    public TemplatePlant setLightEmittance(float f) {
        return (TemplatePlant) super.setLightEmittance(f);
    }

    @Override
    public TemplatePlant setBlastResistance(float resistance) {
        return (TemplatePlant) super.setBlastResistance(resistance);
    }

    @Override
    public TemplatePlant setHardness(float hardness) {
        return (TemplatePlant) super.setHardness(hardness);
    }

    @Override
    public TemplatePlant setUnbreakable() {
        return (TemplatePlant) super.setUnbreakable();
    }

    @Override
    public TemplatePlant setTicksRandomly(boolean ticksRandomly) {
        return (TemplatePlant) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplatePlant setTranslationKey(String string) {
        return (TemplatePlant) super.setTranslationKey(string);
    }

    @Override
    public TemplatePlant disableStat() {
        return (TemplatePlant) super.disableStat();
    }
}
