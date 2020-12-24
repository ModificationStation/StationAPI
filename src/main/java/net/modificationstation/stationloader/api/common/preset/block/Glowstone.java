package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Glowstone extends net.minecraft.block.Glowstone {
    
    public Glowstone(Identifier identifier, int j, Material arg) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, arg);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Glowstone(int i, int j, Material arg) {
        super(i, j, arg);
    }

    @Override
    public Glowstone disableNotifyOnMetaDataChange() {
        return (Glowstone) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Glowstone sounds(BlockSounds sounds) {
        return (Glowstone) super.sounds(sounds);
    }

    @Override
    public Glowstone setLightOpacity(int i) {
        return (Glowstone) super.setLightOpacity(i);
    }

    @Override
    public Glowstone setLightEmittance(float f) {
        return (Glowstone) super.setLightEmittance(f);
    }

    @Override
    public Glowstone setBlastResistance(float resistance) {
        return (Glowstone) super.setBlastResistance(resistance);
    }

    @Override
    public Glowstone setHardness(float hardness) {
        return (Glowstone) super.setHardness(hardness);
    }

    @Override
    public Glowstone setUnbreakable() {
        return (Glowstone) super.setUnbreakable();
    }
}