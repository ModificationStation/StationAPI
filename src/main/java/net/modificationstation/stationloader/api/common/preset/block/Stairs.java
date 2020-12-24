package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Stairs extends net.minecraft.block.Stairs {
    
    public Stairs(Identifier identifier, BlockBase arg) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), arg);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Stairs(int i, BlockBase arg) {
        super(i, arg);
    }

    @Override
    public Stairs disableNotifyOnMetaDataChange() {
        return (Stairs) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Stairs sounds(BlockSounds sounds) {
        return (Stairs) super.sounds(sounds);
    }

    @Override
    public Stairs setLightOpacity(int i) {
        return (Stairs) super.setLightOpacity(i);
    }

    @Override
    public Stairs setLightEmittance(float f) {
        return (Stairs) super.setLightEmittance(f);
    }

    @Override
    public Stairs setBlastResistance(float resistance) {
        return (Stairs) super.setBlastResistance(resistance);
    }

    @Override
    public Stairs setHardness(float hardness) {
        return (Stairs) super.setHardness(hardness);
    }

    @Override
    public Stairs setUnbreakable() {
        return (Stairs) super.setUnbreakable();
    }
}
