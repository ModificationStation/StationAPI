package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Furnace extends net.minecraft.block.Furnace {

    public Furnace(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Furnace(int i, boolean flag) {
        super(i, flag);
    }

    @Override
    public Furnace disableNotifyOnMetaDataChange() {
        return (Furnace) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Furnace sounds(BlockSounds sounds) {
        return (Furnace) super.sounds(sounds);
    }

    @Override
    public Furnace setLightOpacity(int i) {
        return (Furnace) super.setLightOpacity(i);
    }

    @Override
    public Furnace setLightEmittance(float f) {
        return (Furnace) super.setLightEmittance(f);
    }

    @Override
    public Furnace setBlastResistance(float resistance) {
        return (Furnace) super.setBlastResistance(resistance);
    }

    @Override
    public Furnace setHardness(float hardness) {
        return (Furnace) super.setHardness(hardness);
    }

    @Override
    public Furnace setUnbreakable() {
        return (Furnace) super.setUnbreakable();
    }
}
