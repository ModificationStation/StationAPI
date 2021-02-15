package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class LockedChest extends net.minecraft.block.LockedChest implements IBlockTemplate<LockedChest> {

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
    public LockedChest setSounds(BlockSounds sounds) {
        return (LockedChest) super.setSounds(sounds);
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
    public LockedChest setTranslationKey(String string) {
        return (LockedChest) super.setTranslationKey(string);
    }

    @Override
    public LockedChest disableStat() {
        return (LockedChest) super.disableStat();
    }
}
