package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Fire extends net.minecraft.block.Fire {

    public Fire(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Fire(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Fire disableNotifyOnMetaDataChange() {
        return (Fire) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Fire sounds(BlockSounds sounds) {
        return (Fire) super.sounds(sounds);
    }

    @Override
    public Fire setLightOpacity(int i) {
        return (Fire) super.setLightOpacity(i);
    }

    @Override
    public Fire setLightEmittance(float f) {
        return (Fire) super.setLightEmittance(f);
    }

    @Override
    public Fire setBlastResistance(float resistance) {
        return (Fire) super.setBlastResistance(resistance);
    }

    @Override
    public Fire setHardness(float hardness) {
        return (Fire) super.setHardness(hardness);
    }

    @Override
    public Fire setUnbreakable() {
        return (Fire) super.setUnbreakable();
    }

    @Override
    public Fire setTicksRandomly(boolean ticksRandomly) {
        return (Fire) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Fire setName(String string) {
        return (Fire) super.setName(string);
    }

    @Override
    public Fire disableStat() {
        return (Fire) super.disableStat();
    }
}
