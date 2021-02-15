package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Netherrack extends net.minecraft.block.Netherrack implements IBlockTemplate<Netherrack> {

    public Netherrack(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Netherrack(int i, int j) {
        super(i, j);
    }

    @Override
    public Netherrack disableNotifyOnMetaDataChange() {
        return (Netherrack) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Netherrack setSounds(BlockSounds sounds) {
        return (Netherrack) super.setSounds(sounds);
    }

    @Override
    public Netherrack setLightOpacity(int i) {
        return (Netherrack) super.setLightOpacity(i);
    }

    @Override
    public Netherrack setLightEmittance(float f) {
        return (Netherrack) super.setLightEmittance(f);
    }

    @Override
    public Netherrack setBlastResistance(float resistance) {
        return (Netherrack) super.setBlastResistance(resistance);
    }

    @Override
    public Netherrack setHardness(float hardness) {
        return (Netherrack) super.setHardness(hardness);
    }

    @Override
    public Netherrack setUnbreakable() {
        return (Netherrack) super.setUnbreakable();
    }

    @Override
    public Netherrack setTicksRandomly(boolean ticksRandomly) {
        return (Netherrack) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Netherrack setTranslationKey(String string) {
        return (Netherrack) super.setTranslationKey(string);
    }

    @Override
    public Netherrack disableStat() {
        return (Netherrack) super.disableStat();
    }
}
