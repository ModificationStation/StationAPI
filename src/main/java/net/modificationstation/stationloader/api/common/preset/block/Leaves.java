package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Leaves extends net.minecraft.block.Leaves {

    public Leaves(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Leaves(int i, int j) {
        super(i, j);
    }

    @Override
    public Leaves disableNotifyOnMetaDataChange() {
        return (Leaves) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Leaves sounds(BlockSounds sounds) {
        return (Leaves) super.sounds(sounds);
    }

    @Override
    public Leaves setLightOpacity(int i) {
        return (Leaves) super.setLightOpacity(i);
    }

    @Override
    public Leaves setLightEmittance(float f) {
        return (Leaves) super.setLightEmittance(f);
    }

    @Override
    public Leaves setBlastResistance(float resistance) {
        return (Leaves) super.setBlastResistance(resistance);
    }

    @Override
    public Leaves setHardness(float hardness) {
        return (Leaves) super.setHardness(hardness);
    }

    @Override
    public Leaves setUnbreakable() {
        return (Leaves) super.setUnbreakable();
    }
}
