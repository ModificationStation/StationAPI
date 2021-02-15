package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Cake extends net.minecraft.block.Cake implements IBlockTemplate<Cake> {

    public Cake(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Cake(int id, int texture) {
        super(id, texture);
    }

    @Override
    public Cake disableNotifyOnMetaDataChange() {
        return (Cake) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Cake setSounds(BlockSounds sounds) {
        return (Cake) super.setSounds(sounds);
    }

    @Override
    public Cake setLightOpacity(int i) {
        return (Cake) super.setLightOpacity(i);
    }

    @Override
    public Cake setLightEmittance(float f) {
        return (Cake) super.setLightEmittance(f);
    }

    @Override
    public Cake setBlastResistance(float resistance) {
        return (Cake) super.setBlastResistance(resistance);
    }

    @Override
    public Cake setHardness(float hardness) {
        return (Cake) super.setHardness(hardness);
    }

    @Override
    public Cake setUnbreakable() {
        return (Cake) super.setUnbreakable();
    }

    @Override
    public Cake setTicksRandomly(boolean ticksRandomly) {
        return (Cake) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Cake setTranslationKey(String string) {
        return (Cake) super.setTranslationKey(string);
    }

    @Override
    public Cake disableStat() {
        return (Cake) super.disableStat();
    }
}
