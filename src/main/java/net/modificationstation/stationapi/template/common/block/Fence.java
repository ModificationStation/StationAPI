package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Fence extends net.minecraft.block.Fence implements IBlockTemplate<Fence> {

    public Fence(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Fence(int i, int j) {
        super(i, j);
    }

    @Override
    public Fence disableNotifyOnMetaDataChange() {
        return (Fence) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Fence setSounds(BlockSounds sounds) {
        return (Fence) super.setSounds(sounds);
    }

    @Override
    public Fence setLightOpacity(int i) {
        return (Fence) super.setLightOpacity(i);
    }

    @Override
    public Fence setLightEmittance(float f) {
        return (Fence) super.setLightEmittance(f);
    }

    @Override
    public Fence setBlastResistance(float resistance) {
        return (Fence) super.setBlastResistance(resistance);
    }

    @Override
    public Fence setHardness(float hardness) {
        return (Fence) super.setHardness(hardness);
    }

    @Override
    public Fence setUnbreakable() {
        return (Fence) super.setUnbreakable();
    }

    @Override
    public Fence setTicksRandomly(boolean ticksRandomly) {
        return (Fence) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Fence setTranslationKey(String string) {
        return (Fence) super.setTranslationKey(string);
    }

    @Override
    public Fence disableStat() {
        return (Fence) super.disableStat();
    }
}
