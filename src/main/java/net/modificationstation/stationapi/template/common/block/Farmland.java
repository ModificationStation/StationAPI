package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Farmland extends net.minecraft.block.Farmland implements IBlockTemplate<Farmland> {

    public Farmland(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Farmland(int id) {
        super(id);
    }

    @Override
    public Farmland disableNotifyOnMetaDataChange() {
        return (Farmland) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Farmland setSounds(BlockSounds sounds) {
        return (Farmland) super.setSounds(sounds);
    }

    @Override
    public Farmland setLightOpacity(int i) {
        return (Farmland) super.setLightOpacity(i);
    }

    @Override
    public Farmland setLightEmittance(float f) {
        return (Farmland) super.setLightEmittance(f);
    }

    @Override
    public Farmland setBlastResistance(float resistance) {
        return (Farmland) super.setBlastResistance(resistance);
    }

    @Override
    public Farmland setHardness(float hardness) {
        return (Farmland) super.setHardness(hardness);
    }

    @Override
    public Farmland setUnbreakable() {
        return (Farmland) super.setUnbreakable();
    }

    @Override
    public Farmland setTicksRandomly(boolean ticksRandomly) {
        return (Farmland) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Farmland setTranslationKey(String string) {
        return (Farmland) super.setTranslationKey(string);
    }

    @Override
    public Farmland disableStat() {
        return (Farmland) super.disableStat();
    }
}
