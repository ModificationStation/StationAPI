package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Ladder extends net.minecraft.block.Ladder {

    public Ladder(Identifier identifier, int texUVStart) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texUVStart);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Ladder(int id, int texUVStart) {
        super(id, texUVStart);
    }

    @Override
    public Ladder disableNotifyOnMetaDataChange() {
        return (Ladder) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Ladder sounds(BlockSounds sounds) {
        return (Ladder) super.sounds(sounds);
    }

    @Override
    public Ladder setLightOpacity(int i) {
        return (Ladder) super.setLightOpacity(i);
    }

    @Override
    public Ladder setLightEmittance(float f) {
        return (Ladder) super.setLightEmittance(f);
    }

    @Override
    public Ladder setBlastResistance(float resistance) {
        return (Ladder) super.setBlastResistance(resistance);
    }

    @Override
    public Ladder setHardness(float hardness) {
        return (Ladder) super.setHardness(hardness);
    }

    @Override
    public Ladder setUnbreakable() {
        return (Ladder) super.setUnbreakable();
    }

    @Override
    public Ladder setTicksRandomly(boolean ticksRandomly) {
        return (Ladder) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Ladder setName(String string) {
        return (Ladder) super.setName(string);
    }

    @Override
    public Ladder disableStat() {
        return (Ladder) super.disableStat();
    }
}
