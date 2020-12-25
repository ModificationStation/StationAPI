package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Cobweb extends net.minecraft.block.Cobweb {

    public Cobweb(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Cobweb(int i, int j) {
        super(i, j);
    }

    @Override
    public Cobweb disableNotifyOnMetaDataChange() {
        return (Cobweb) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Cobweb sounds(BlockSounds sounds) {
        return (Cobweb) super.sounds(sounds);
    }

    @Override
    public Cobweb setLightOpacity(int i) {
        return (Cobweb) super.setLightOpacity(i);
    }

    @Override
    public Cobweb setLightEmittance(float f) {
        return (Cobweb) super.setLightEmittance(f);
    }

    @Override
    public Cobweb setBlastResistance(float resistance) {
        return (Cobweb) super.setBlastResistance(resistance);
    }

    @Override
    public Cobweb setHardness(float hardness) {
        return (Cobweb) super.setHardness(hardness);
    }

    @Override
    public Cobweb setUnbreakable() {
        return (Cobweb) super.setUnbreakable();
    }

    @Override
    public Cobweb setTicksRandomly(boolean ticksRandomly) {
        return (Cobweb) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Cobweb setName(String string) {
        return (Cobweb) super.setName(string);
    }

    @Override
    public Cobweb disableStat() {
        return (Cobweb) super.disableStat();
    }
}
