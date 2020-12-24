package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Lever extends net.minecraft.block.Lever {

    public Lever(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Lever(int i, int j) {
        super(i, j);
    }

    @Override
    public Lever disableNotifyOnMetaDataChange() {
        return (Lever) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Lever sounds(BlockSounds sounds) {
        return (Lever) super.sounds(sounds);
    }

    @Override
    public Lever setLightOpacity(int i) {
        return (Lever) super.setLightOpacity(i);
    }

    @Override
    public Lever setLightEmittance(float f) {
        return (Lever) super.setLightEmittance(f);
    }

    @Override
    public Lever setBlastResistance(float resistance) {
        return (Lever) super.setBlastResistance(resistance);
    }

    @Override
    public Lever setHardness(float hardness) {
        return (Lever) super.setHardness(hardness);
    }

    @Override
    public Lever setUnbreakable() {
        return (Lever) super.setUnbreakable();
    }

    @Override
    public Lever setTicksRandomly(boolean ticksRandomly) {
        return (Lever) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Lever setName(String string) {
        return (Lever) super.setName(string);
    }

    @Override
    public Lever disableStat() {
        return (Lever) super.disableStat();
    }
}
