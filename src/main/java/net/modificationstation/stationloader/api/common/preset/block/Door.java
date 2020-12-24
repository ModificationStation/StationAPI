package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Door extends net.minecraft.block.Door {

    public Door(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Door(int i, Material arg) {
        super(i, arg);
    }
    
    @Override
    public Door disableNotifyOnMetaDataChange() {
        return (Door) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Door sounds(BlockSounds sounds) {
        return (Door) super.sounds(sounds);
    }

    @Override
    public Door setLightOpacity(int i) {
        return (Door) super.setLightOpacity(i);
    }

    @Override
    public Door setLightEmittance(float f) {
        return (Door) super.setLightEmittance(f);
    }

    @Override
    public Door setBlastResistance(float resistance) {
        return (Door) super.setBlastResistance(resistance);
    }

    @Override
    public Door setHardness(float hardness) {
        return (Door) super.setHardness(hardness);
    }

    @Override
    public Door setUnbreakable() {
        return (Door) super.setUnbreakable();
    }
}
