package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class DeadBush extends net.minecraft.block.DeadBush {

    public DeadBush(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public DeadBush(int i, int j) {
        super(i, j);
    }

    @Override
    public DeadBush disableNotifyOnMetaDataChange() {
        return (DeadBush) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public DeadBush sounds(BlockSounds sounds) {
        return (DeadBush) super.sounds(sounds);
    }

    @Override
    public DeadBush setLightOpacity(int i) {
        return (DeadBush) super.setLightOpacity(i);
    }

    @Override
    public DeadBush setLightEmittance(float f) {
        return (DeadBush) super.setLightEmittance(f);
    }

    @Override
    public DeadBush setBlastResistance(float resistance) {
        return (DeadBush) super.setBlastResistance(resistance);
    }

    @Override
    public DeadBush setHardness(float hardness) {
        return (DeadBush) super.setHardness(hardness);
    }

    @Override
    public DeadBush setUnbreakable() {
        return (DeadBush) super.setUnbreakable();
    }

    @Override
    public DeadBush setTicksRandomly(boolean ticksRandomly) {
        return (DeadBush) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public DeadBush setName(String string) {
        return (DeadBush) super.setName(string);
    }

    @Override
    public DeadBush disableStat() {
        return (DeadBush) super.disableStat();
    }
}
