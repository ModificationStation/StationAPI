package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class LockedChest extends net.minecraft.block.LockedChest {

    public LockedChest(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public LockedChest(int i) {
        super(i);
    }

    @Override
    public LockedChest disableNotifyOnMetaDataChange() {
        return (LockedChest) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public LockedChest sounds(BlockSounds sounds) {
        return (LockedChest) super.sounds(sounds);
    }

    @Override
    public LockedChest setLightOpacity(int i) {
        return (LockedChest) super.setLightOpacity(i);
    }

    @Override
    public LockedChest setLightEmittance(float f) {
        return (LockedChest) super.setLightEmittance(f);
    }

    @Override
    public LockedChest setBlastResistance(float resistance) {
        return (LockedChest) super.setBlastResistance(resistance);
    }

    @Override
    public LockedChest setHardness(float hardness) {
        return (LockedChest) super.setHardness(hardness);
    }

    @Override
    public LockedChest setUnbreakable() {
        return (LockedChest) super.setUnbreakable();
    }

    @Override
    public LockedChest setTicksRandomly(boolean ticksRandomly) {
        return (LockedChest) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public LockedChest setName(String string) {
        return (LockedChest) super.setName(string);
    }

    @Override
    public LockedChest disableStat() {
        return (LockedChest) super.disableStat();
    }
}
