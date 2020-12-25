package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Fence extends net.minecraft.block.Fence {

    public Fence(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Fence(int i, int j) {
        super(i, j);
    }

    @Override
    public Fence disableNotifyOnMetaDataChange() {
        return (Fence) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Fence sounds(BlockSounds sounds) {
        return (Fence) super.sounds(sounds);
    }

    @Override
    public Fence setLightOpacity(int i) {
        return (Fence) super.setLightOpacity(i);
    }

    @Override
    public Fence setLightEmittance(float f) {
        return (Fence) super.setLightEmittance(f);
    }

    @Override
    public Fence setBlastResistance(float resistance) {
        return (Fence) super.setBlastResistance(resistance);
    }

    @Override
    public Fence setHardness(float hardness) {
        return (Fence) super.setHardness(hardness);
    }

    @Override
    public Fence setUnbreakable() {
        return (Fence) super.setUnbreakable();
    }

    @Override
    public Fence setTicksRandomly(boolean ticksRandomly) {
        return (Fence) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Fence setName(String string) {
        return (Fence) super.setName(string);
    }

    @Override
    public Fence disableStat() {
        return (Fence) super.disableStat();
    }
}
