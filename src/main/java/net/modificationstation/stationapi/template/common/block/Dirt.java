package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Dirt extends net.minecraft.block.Dirt implements IBlockTemplate<Dirt> {

    public Dirt(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Dirt(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Dirt disableNotifyOnMetaDataChange() {
        return (Dirt) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Dirt setSounds(BlockSounds sounds) {
        return (Dirt) super.setSounds(sounds);
    }

    @Override
    public Dirt setLightOpacity(int i) {
        return (Dirt) super.setLightOpacity(i);
    }

    @Override
    public Dirt setLightEmittance(float f) {
        return (Dirt) super.setLightEmittance(f);
    }

    @Override
    public Dirt setBlastResistance(float resistance) {
        return (Dirt) super.setBlastResistance(resistance);
    }

    @Override
    public Dirt setHardness(float hardness) {
        return (Dirt) super.setHardness(hardness);
    }

    @Override
    public Dirt setUnbreakable() {
        return (Dirt) super.setUnbreakable();
    }

    @Override
    public Dirt setTicksRandomly(boolean ticksRandomly) {
        return (Dirt) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Dirt setTranslationKey(String string) {
        return (Dirt) super.setTranslationKey(string);
    }

    @Override
    public Dirt disableStat() {
        return (Dirt) super.disableStat();
    }
}
