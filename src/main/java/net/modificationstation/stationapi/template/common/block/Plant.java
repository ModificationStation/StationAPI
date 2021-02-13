package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Plant extends net.minecraft.block.Plant {

    public Plant(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Plant(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Plant disableNotifyOnMetaDataChange() {
        return (Plant) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Plant sounds(BlockSounds sounds) {
        return (Plant) super.setSounds(sounds);
    }

    @Override
    public Plant setLightOpacity(int i) {
        return (Plant) super.setLightOpacity(i);
    }

    @Override
    public Plant setLightEmittance(float f) {
        return (Plant) super.setLightEmittance(f);
    }

    @Override
    public Plant setBlastResistance(float resistance) {
        return (Plant) super.setBlastResistance(resistance);
    }

    @Override
    public Plant setHardness(float hardness) {
        return (Plant) super.setHardness(hardness);
    }

    @Override
    public Plant setUnbreakable() {
        return (Plant) super.setUnbreakable();
    }

    @Override
    public Plant setTicksRandomly(boolean ticksRandomly) {
        return (Plant) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Plant setName(String string) {
        return (Plant) super.setTranslationKey(string);
    }

    @Override
    public Plant disableStat() {
        return (Plant) super.disableStat();
    }
}
