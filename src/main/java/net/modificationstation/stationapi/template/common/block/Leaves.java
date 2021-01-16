package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Leaves extends net.minecraft.block.Leaves {

    public Leaves(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Leaves(int i, int j) {
        super(i, j);
    }

    @Override
    public Leaves disableNotifyOnMetaDataChange() {
        return (Leaves) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Leaves sounds(BlockSounds sounds) {
        return (Leaves) super.sounds(sounds);
    }

    @Override
    public Leaves setLightOpacity(int i) {
        return (Leaves) super.setLightOpacity(i);
    }

    @Override
    public Leaves setLightEmittance(float f) {
        return (Leaves) super.setLightEmittance(f);
    }

    @Override
    public Leaves setBlastResistance(float resistance) {
        return (Leaves) super.setBlastResistance(resistance);
    }

    @Override
    public Leaves setHardness(float hardness) {
        return (Leaves) super.setHardness(hardness);
    }

    @Override
    public Leaves setUnbreakable() {
        return (Leaves) super.setUnbreakable();
    }

    @Override
    public Leaves setTicksRandomly(boolean ticksRandomly) {
        return (Leaves) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Leaves setName(String string) {
        return (Leaves) super.setName(string);
    }

    @Override
    public Leaves disableStat() {
        return (Leaves) super.disableStat();
    }
}
