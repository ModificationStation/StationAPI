package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Mushroom extends net.minecraft.block.Mushroom {

    public Mushroom(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Mushroom(int i, int j) {
        super(i, j);
    }

    @Override
    public Mushroom disableNotifyOnMetaDataChange() {
        return (Mushroom) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Mushroom sounds(BlockSounds sounds) {
        return (Mushroom) super.setSounds(sounds);
    }

    @Override
    public Mushroom setLightOpacity(int i) {
        return (Mushroom) super.setLightOpacity(i);
    }

    @Override
    public Mushroom setLightEmittance(float f) {
        return (Mushroom) super.setLightEmittance(f);
    }

    @Override
    public Mushroom setBlastResistance(float resistance) {
        return (Mushroom) super.setBlastResistance(resistance);
    }

    @Override
    public Mushroom setHardness(float hardness) {
        return (Mushroom) super.setHardness(hardness);
    }

    @Override
    public Mushroom setUnbreakable() {
        return (Mushroom) super.setUnbreakable();
    }

    @Override
    public Mushroom setTicksRandomly(boolean ticksRandomly) {
        return (Mushroom) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Mushroom setName(String string) {
        return (Mushroom) super.setTranslationKey(string);
    }

    @Override
    public Mushroom disableStat() {
        return (Mushroom) super.disableStat();
    }
}
