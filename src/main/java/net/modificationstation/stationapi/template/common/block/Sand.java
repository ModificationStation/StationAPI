package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

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
    public Sand setSounds(BlockSounds sounds) {
        return (Sand) super.setSounds(sounds);
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
    public Sand setTranslationKey(String string) {
        return (Sand) super.setTranslationKey(string);
    }

    @Override
    public Sand disableStat() {
        return (Sand) super.disableStat();
    }
}
