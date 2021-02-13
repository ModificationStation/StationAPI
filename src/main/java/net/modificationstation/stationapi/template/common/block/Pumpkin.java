package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Pumpkin extends net.minecraft.block.Pumpkin {

    public Pumpkin(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Pumpkin(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public Pumpkin disableNotifyOnMetaDataChange() {
        return (Pumpkin) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Pumpkin sounds(BlockSounds sounds) {
        return (Pumpkin) super.setSounds(sounds);
    }

    @Override
    public Pumpkin setLightOpacity(int i) {
        return (Pumpkin) super.setLightOpacity(i);
    }

    @Override
    public Pumpkin setLightEmittance(float f) {
        return (Pumpkin) super.setLightEmittance(f);
    }

    @Override
    public Pumpkin setBlastResistance(float resistance) {
        return (Pumpkin) super.setBlastResistance(resistance);
    }

    @Override
    public Pumpkin setHardness(float hardness) {
        return (Pumpkin) super.setHardness(hardness);
    }

    @Override
    public Pumpkin setUnbreakable() {
        return (Pumpkin) super.setUnbreakable();
    }

    @Override
    public Pumpkin setTicksRandomly(boolean ticksRandomly) {
        return (Pumpkin) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Pumpkin setName(String string) {
        return (Pumpkin) super.setTranslationKey(string);
    }

    @Override
    public Pumpkin disableStat() {
        return (Pumpkin) super.disableStat();
    }
}
