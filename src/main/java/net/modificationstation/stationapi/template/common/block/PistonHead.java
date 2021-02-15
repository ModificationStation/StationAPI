package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class PistonHead extends net.minecraft.block.PistonHead implements IBlockTemplate<PistonHead> {

    public PistonHead(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public PistonHead(int i, int j) {
        super(i, j);
    }

    @Override
    public PistonHead disableNotifyOnMetaDataChange() {
        return (PistonHead) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public PistonHead setSounds(BlockSounds sounds) {
        return (PistonHead) super.setSounds(sounds);
    }

    @Override
    public PistonHead setLightOpacity(int i) {
        return (PistonHead) super.setLightOpacity(i);
    }

    @Override
    public PistonHead setLightEmittance(float f) {
        return (PistonHead) super.setLightEmittance(f);
    }

    @Override
    public PistonHead setBlastResistance(float resistance) {
        return (PistonHead) super.setBlastResistance(resistance);
    }

    @Override
    public PistonHead setHardness(float hardness) {
        return (PistonHead) super.setHardness(hardness);
    }

    @Override
    public PistonHead setUnbreakable() {
        return (PistonHead) super.setUnbreakable();
    }

    @Override
    public PistonHead setTicksRandomly(boolean ticksRandomly) {
        return (PistonHead) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public PistonHead setTranslationKey(String string) {
        return (PistonHead) super.setTranslationKey(string);
    }

    @Override
    public PistonHead disableStat() {
        return (PistonHead) super.disableStat();
    }
}
