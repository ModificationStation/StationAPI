package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Sapling extends net.minecraft.block.Sapling {

    public Sapling(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Sapling(int i, int j) {
        super(i, j);
    }

    @Override
    public Sapling disableNotifyOnMetaDataChange() {
        return (Sapling) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Sapling sounds(BlockSounds sounds) {
        return (Sapling) super.setSounds(sounds);
    }

    @Override
    public Sapling setLightOpacity(int i) {
        return (Sapling) super.setLightOpacity(i);
    }

    @Override
    public Sapling setLightEmittance(float f) {
        return (Sapling) super.setLightEmittance(f);
    }

    @Override
    public Sapling setBlastResistance(float resistance) {
        return (Sapling) super.setBlastResistance(resistance);
    }

    @Override
    public Sapling setHardness(float hardness) {
        return (Sapling) super.setHardness(hardness);
    }

    @Override
    public Sapling setUnbreakable() {
        return (Sapling) super.setUnbreakable();
    }

    @Override
    public Sapling setTicksRandomly(boolean ticksRandomly) {
        return (Sapling) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Sapling setName(String string) {
        return (Sapling) super.setTranslationKey(string);
    }

    @Override
    public Sapling disableStat() {
        return (Sapling) super.disableStat();
    }
}
