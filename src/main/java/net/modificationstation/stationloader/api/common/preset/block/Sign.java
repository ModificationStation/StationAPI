package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Sign extends net.minecraft.block.Sign {
    
    public Sign(Identifier identifier, Class<? extends TileEntityBase> arg, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), arg, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Sign(int i, Class<? extends TileEntityBase> arg, boolean flag) {
        super(i, arg, flag);
    }

    @Override
    public Sign disableNotifyOnMetaDataChange() {
        return (Sign) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Sign sounds(BlockSounds sounds) {
        return (Sign) super.sounds(sounds);
    }

    @Override
    public Sign setLightOpacity(int i) {
        return (Sign) super.setLightOpacity(i);
    }

    @Override
    public Sign setLightEmittance(float f) {
        return (Sign) super.setLightEmittance(f);
    }

    @Override
    public Sign setBlastResistance(float resistance) {
        return (Sign) super.setBlastResistance(resistance);
    }

    @Override
    public Sign setHardness(float hardness) {
        return (Sign) super.setHardness(hardness);
    }

    @Override
    public Sign setUnbreakable() {
        return (Sign) super.setUnbreakable();
    }

    @Override
    public Sign setTicksRandomly(boolean ticksRandomly) {
        return (Sign) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Sign setName(String string) {
        return (Sign) super.setName(string);
    }

    @Override
    public Sign disableStat() {
        return (Sign) super.disableStat();
    }
}
