package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Piston extends net.minecraft.block.Piston {

    public Piston(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Piston(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public Piston disableNotifyOnMetaDataChange() {
        return (Piston) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Piston sounds(BlockSounds sounds) {
        return (Piston) super.setSounds(sounds);
    }

    @Override
    public Piston setLightOpacity(int i) {
        return (Piston) super.setLightOpacity(i);
    }

    @Override
    public Piston setLightEmittance(float f) {
        return (Piston) super.setLightEmittance(f);
    }

    @Override
    public Piston setBlastResistance(float resistance) {
        return (Piston) super.setBlastResistance(resistance);
    }

    @Override
    public Piston setHardness(float hardness) {
        return (Piston) super.setHardness(hardness);
    }

    @Override
    public Piston setUnbreakable() {
        return (Piston) super.setUnbreakable();
    }

    @Override
    public Piston setTicksRandomly(boolean ticksRandomly) {
        return (Piston) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Piston setName(String string) {
        return (Piston) super.setTranslationKey(string);
    }

    @Override
    public Piston disableStat() {
        return (Piston) super.disableStat();
    }
}
