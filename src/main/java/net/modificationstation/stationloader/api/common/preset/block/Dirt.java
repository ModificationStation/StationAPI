package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Dirt extends net.minecraft.block.Dirt {

    public Dirt(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Dirt(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Dirt disableNotifyOnMetaDataChange() {
        return (Dirt) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Dirt sounds(BlockSounds sounds) {
        return (Dirt) super.sounds(sounds);
    }

    @Override
    public Dirt setLightOpacity(int i) {
        return (Dirt) super.setLightOpacity(i);
    }

    @Override
    public Dirt setLightEmittance(float f) {
        return (Dirt) super.setLightEmittance(f);
    }

    @Override
    public Dirt setBlastResistance(float resistance) {
        return (Dirt) super.setBlastResistance(resistance);
    }

    @Override
    public Dirt setHardness(float hardness) {
        return (Dirt) super.setHardness(hardness);
    }

    @Override
    public Dirt setUnbreakable() {
        return (Dirt) super.setUnbreakable();
    }

    @Override
    public Dirt setTicksRandomly(boolean ticksRandomly) {
        return (Dirt) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Dirt setName(String string) {
        return (Dirt) super.setName(string);
    }

    @Override
    public Dirt disableStat() {
        return (Dirt) super.disableStat();
    }
}
