package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Chest extends net.minecraft.block.Chest {

    public Chest(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Chest(int id) {
        super(id);
    }

    @Override
    public Chest disableNotifyOnMetaDataChange() {
        return (Chest) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Chest sounds(BlockSounds sounds) {
        return (Chest) super.sounds(sounds);
    }

    @Override
    public Chest setLightOpacity(int i) {
        return (Chest) super.setLightOpacity(i);
    }

    @Override
    public Chest setLightEmittance(float f) {
        return (Chest) super.setLightEmittance(f);
    }

    @Override
    public Chest setBlastResistance(float resistance) {
        return (Chest) super.setBlastResistance(resistance);
    }

    @Override
    public Chest setHardness(float hardness) {
        return (Chest) super.setHardness(hardness);
    }

    @Override
    public Chest setUnbreakable() {
        return (Chest) super.setUnbreakable();
    }

    @Override
    public Chest setTicksRandomly(boolean ticksRandomly) {
        return (Chest) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Chest setName(String string) {
        return (Chest) super.setName(string);
    }

    @Override
    public Chest disableStat() {
        return (Chest) super.disableStat();
    }
}
