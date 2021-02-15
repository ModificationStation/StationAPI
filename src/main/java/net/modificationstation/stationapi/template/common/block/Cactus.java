package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Cactus extends net.minecraft.block.Cactus implements IBlockTemplate<Cactus> {

    public Cactus(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Cactus(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Cactus disableNotifyOnMetaDataChange() {
        return (Cactus) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Cactus setSounds(BlockSounds sounds) {
        return (Cactus) super.setSounds(sounds);
    }

    @Override
    public Cactus setLightOpacity(int i) {
        return (Cactus) super.setLightOpacity(i);
    }

    @Override
    public Cactus setLightEmittance(float f) {
        return (Cactus) super.setLightEmittance(f);
    }

    @Override
    public Cactus setBlastResistance(float resistance) {
        return (Cactus) super.setBlastResistance(resistance);
    }

    @Override
    public Cactus setHardness(float hardness) {
        return (Cactus) super.setHardness(hardness);
    }

    @Override
    public Cactus setUnbreakable() {
        return (Cactus) super.setUnbreakable();
    }

    @Override
    public Cactus setTicksRandomly(boolean ticksRandomly) {
        return (Cactus) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Cactus setTranslationKey(String string) {
        return (Cactus) super.setTranslationKey(string);
    }

    @Override
    public Cactus disableStat() {
        return (Cactus) super.disableStat();
    }
}
