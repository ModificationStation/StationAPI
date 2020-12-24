package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Farmland extends net.minecraft.block.Farmland {

    public Farmland(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Farmland(int id) {
        super(id);
    }

    @Override
    public Farmland disableNotifyOnMetaDataChange() {
        return (Farmland) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Farmland sounds(BlockSounds sounds) {
        return (Farmland) super.sounds(sounds);
    }

    @Override
    public Farmland setLightOpacity(int i) {
        return (Farmland) super.setLightOpacity(i);
    }

    @Override
    public Farmland setLightEmittance(float f) {
        return (Farmland) super.setLightEmittance(f);
    }

    @Override
    public Farmland setBlastResistance(float resistance) {
        return (Farmland) super.setBlastResistance(resistance);
    }

    @Override
    public Farmland setHardness(float hardness) {
        return (Farmland) super.setHardness(hardness);
    }

    @Override
    public Farmland setUnbreakable() {
        return (Farmland) super.setUnbreakable();
    }
}