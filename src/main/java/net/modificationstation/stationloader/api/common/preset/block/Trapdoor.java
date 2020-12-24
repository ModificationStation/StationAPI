package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Trapdoor extends net.minecraft.block.Trapdoor {
    
    public Trapdoor(Identifier identifier, Material arg) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), arg);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Trapdoor(int i, Material arg) {
        super(i, arg);
    }

    @Override
    public Trapdoor disableNotifyOnMetaDataChange() {
        return (Trapdoor) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Trapdoor sounds(BlockSounds sounds) {
        return (Trapdoor) super.sounds(sounds);
    }

    @Override
    public Trapdoor setLightOpacity(int i) {
        return (Trapdoor) super.setLightOpacity(i);
    }

    @Override
    public Trapdoor setLightEmittance(float f) {
        return (Trapdoor) super.setLightEmittance(f);
    }

    @Override
    public Trapdoor setBlastResistance(float resistance) {
        return (Trapdoor) super.setBlastResistance(resistance);
    }

    @Override
    public Trapdoor setHardness(float hardness) {
        return (Trapdoor) super.setHardness(hardness);
    }

    @Override
    public Trapdoor setUnbreakable() {
        return (Trapdoor) super.setUnbreakable();
    }
}
