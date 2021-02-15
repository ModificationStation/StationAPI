package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Trapdoor extends net.minecraft.block.Trapdoor implements IBlockTemplate<Trapdoor> {
    
    public Trapdoor(Identifier identifier, Material arg) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), arg);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public Trapdoor(int i, Material arg) {
        super(i, arg);
    }

    @Override
    public Trapdoor disableNotifyOnMetaDataChange() {
        return (Trapdoor) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Trapdoor setSounds(BlockSounds sounds) {
        return (Trapdoor) super.setSounds(sounds);
    }

    @Override
    public Trapdoor setLightOpacity(int i) {
        return (Trapdoor) super.setLightOpacity(i);
    }

    @Override
    public Trapdoor setLightEmittance(float f) {
        return (Trapdoor) super.setLightEmittance(f);
    }

    @Override
    public Trapdoor setBlastResistance(float resistance) {
        return (Trapdoor) super.setBlastResistance(resistance);
    }

    @Override
    public Trapdoor setHardness(float hardness) {
        return (Trapdoor) super.setHardness(hardness);
    }

    @Override
    public Trapdoor setUnbreakable() {
        return (Trapdoor) super.setUnbreakable();
    }

    @Override
    public Trapdoor setTicksRandomly(boolean ticksRandomly) {
        return (Trapdoor) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Trapdoor setTranslationKey(String string) {
        return (Trapdoor) super.setTranslationKey(string);
    }

    @Override
    public Trapdoor disableStat() {
        return (Trapdoor) super.disableStat();
    }
}
