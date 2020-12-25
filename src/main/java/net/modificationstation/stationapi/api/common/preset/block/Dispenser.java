package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Dispenser extends net.minecraft.block.Dispenser {

    public Dispenser(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Dispenser(int id) {
        super(id);
    }

    @Override
    public Dispenser disableNotifyOnMetaDataChange() {
        return (Dispenser) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Dispenser sounds(BlockSounds sounds) {
        return (Dispenser) super.sounds(sounds);
    }

    @Override
    public Dispenser setLightOpacity(int i) {
        return (Dispenser) super.setLightOpacity(i);
    }

    @Override
    public Dispenser setLightEmittance(float f) {
        return (Dispenser) super.setLightEmittance(f);
    }

    @Override
    public Dispenser setBlastResistance(float resistance) {
        return (Dispenser) super.setBlastResistance(resistance);
    }

    @Override
    public Dispenser setHardness(float hardness) {
        return (Dispenser) super.setHardness(hardness);
    }

    @Override
    public Dispenser setUnbreakable() {
        return (Dispenser) super.setUnbreakable();
    }

    @Override
    public Dispenser setTicksRandomly(boolean ticksRandomly) {
        return (Dispenser) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Dispenser setName(String string) {
        return (Dispenser) super.setName(string);
    }

    @Override
    public Dispenser disableStat() {
        return (Dispenser) super.disableStat();
    }
}
