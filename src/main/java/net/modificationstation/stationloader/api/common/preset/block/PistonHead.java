package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class PistonHead extends net.minecraft.block.PistonHead {

    public PistonHead(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public PistonHead(int i, int j) {
        super(i, j);
    }

    @Override
    public PistonHead disableNotifyOnMetaDataChange() {
        return (PistonHead) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public PistonHead sounds(BlockSounds sounds) {
        return (PistonHead) super.sounds(sounds);
    }

    @Override
    public PistonHead setLightOpacity(int i) {
        return (PistonHead) super.setLightOpacity(i);
    }

    @Override
    public PistonHead setLightEmittance(float f) {
        return (PistonHead) super.setLightEmittance(f);
    }

    @Override
    public PistonHead setBlastResistance(float resistance) {
        return (PistonHead) super.setBlastResistance(resistance);
    }

    @Override
    public PistonHead setHardness(float hardness) {
        return (PistonHead) super.setHardness(hardness);
    }

    @Override
    public PistonHead setUnbreakable() {
        return (PistonHead) super.setUnbreakable();
    }
}
