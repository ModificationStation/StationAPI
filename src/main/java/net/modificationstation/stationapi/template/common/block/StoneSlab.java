package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class StoneSlab extends net.minecraft.block.StoneSlab implements IBlockTemplate<StoneSlab> {
    
    public StoneSlab(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public StoneSlab(int i, boolean flag) {
        super(i, flag);
    }

    @Override
    public StoneSlab disableNotifyOnMetaDataChange() {
        return (StoneSlab) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public StoneSlab setSounds(BlockSounds sounds) {
        return (StoneSlab) super.setSounds(sounds);
    }

    @Override
    public StoneSlab setLightOpacity(int i) {
        return (StoneSlab) super.setLightOpacity(i);
    }

    @Override
    public StoneSlab setLightEmittance(float f) {
        return (StoneSlab) super.setLightEmittance(f);
    }

    @Override
    public StoneSlab setBlastResistance(float resistance) {
        return (StoneSlab) super.setBlastResistance(resistance);
    }

    @Override
    public StoneSlab setHardness(float hardness) {
        return (StoneSlab) super.setHardness(hardness);
    }

    @Override
    public StoneSlab setUnbreakable() {
        return (StoneSlab) super.setUnbreakable();
    }

    @Override
    public StoneSlab setTicksRandomly(boolean ticksRandomly) {
        return (StoneSlab) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public StoneSlab setTranslationKey(String string) {
        return (StoneSlab) super.setTranslationKey(string);
    }

    @Override
    public StoneSlab disableStat() {
        return (StoneSlab) super.disableStat();
    }
}
