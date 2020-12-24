package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Netherrack extends net.minecraft.block.Netherrack {

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
    public Netherrack sounds(BlockSounds sounds) {
        return (Netherrack) super.sounds(sounds);
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
    public Netherrack setName(String string) {
        return (Netherrack) super.setName(string);
    }

    @Override
    public Netherrack disableStat() {
        return (Netherrack) super.disableStat();
    }
}
