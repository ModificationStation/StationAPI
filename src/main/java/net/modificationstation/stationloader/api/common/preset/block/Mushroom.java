package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Mushroom extends net.minecraft.block.Mushroom {

    public Mushroom(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Mushroom(int i, int j) {
        super(i, j);
    }

    @Override
    public Mushroom disableNotifyOnMetaDataChange() {
        return (Mushroom) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Mushroom sounds(BlockSounds sounds) {
        return (Mushroom) super.sounds(sounds);
    }

    @Override
    public Mushroom setLightOpacity(int i) {
        return (Mushroom) super.setLightOpacity(i);
    }

    @Override
    public Mushroom setLightEmittance(float f) {
        return (Mushroom) super.setLightEmittance(f);
    }

    @Override
    public Mushroom setBlastResistance(float resistance) {
        return (Mushroom) super.setBlastResistance(resistance);
    }

    @Override
    public Mushroom setHardness(float hardness) {
        return (Mushroom) super.setHardness(hardness);
    }

    @Override
    public Mushroom setUnbreakable() {
        return (Mushroom) super.setUnbreakable();
    }
}
