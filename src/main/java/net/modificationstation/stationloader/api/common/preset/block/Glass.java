package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Glass extends net.minecraft.block.Glass {

    public Glass(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, arg, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Glass(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }

    @Override
    public Glass disableNotifyOnMetaDataChange() {
        return (Glass) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Glass sounds(BlockSounds sounds) {
        return (Glass) super.sounds(sounds);
    }

    @Override
    public Glass setLightOpacity(int i) {
        return (Glass) super.setLightOpacity(i);
    }

    @Override
    public Glass setLightEmittance(float f) {
        return (Glass) super.setLightEmittance(f);
    }

    @Override
    public Glass setBlastResistance(float resistance) {
        return (Glass) super.setBlastResistance(resistance);
    }

    @Override
    public Glass setHardness(float hardness) {
        return (Glass) super.setHardness(hardness);
    }

    @Override
    public Glass setUnbreakable() {
        return (Glass) super.setUnbreakable();
    }

    @Override
    public Glass setTicksRandomly(boolean ticksRandomly) {
        return (Glass) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Glass setName(String string) {
        return (Glass) super.setName(string);
    }

    @Override
    public Glass disableStat() {
        return (Glass) super.disableStat();
    }
}
