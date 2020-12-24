package net.modificationstation.stationloader.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationloader.api.common.block.BlockRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;

public class Sand extends net.minecraft.block.Sand {
    
    public Sand(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Sand(int i, int j) {
        super(i, j);
    }

    @Override
    public Sand disableNotifyOnMetaDataChange() {
        return (Sand) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Sand sounds(BlockSounds sounds) {
        return (Sand) super.sounds(sounds);
    }

    @Override
    public Sand setLightOpacity(int i) {
        return (Sand) super.setLightOpacity(i);
    }

    @Override
    public Sand setLightEmittance(float f) {
        return (Sand) super.setLightEmittance(f);
    }

    @Override
    public Sand setBlastResistance(float resistance) {
        return (Sand) super.setBlastResistance(resistance);
    }

    @Override
    public Sand setHardness(float hardness) {
        return (Sand) super.setHardness(hardness);
    }

    @Override
    public Sand setUnbreakable() {
        return (Sand) super.setUnbreakable();
    }

    @Override
    public Sand setTicksRandomly(boolean ticksRandomly) {
        return (Sand) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Sand setName(String string) {
        return (Sand) super.setName(string);
    }

    @Override
    public Sand disableStat() {
        return (Sand) super.disableStat();
    }
}
