package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class Door extends net.minecraft.block.Door {

    public Door(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public Door(int i, Material arg) {
        super(i, arg);
    }
    
    @Override
    public Door disableNotifyOnMetaDataChange() {
        return (Door) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public Door setSounds(BlockSounds sounds) {
        return (Door) super.setSounds(sounds);
    }

    @Override
    public Door setLightOpacity(int i) {
        return (Door) super.setLightOpacity(i);
    }

    @Override
    public Door setLightEmittance(float f) {
        return (Door) super.setLightEmittance(f);
    }

    @Override
    public Door setBlastResistance(float resistance) {
        return (Door) super.setBlastResistance(resistance);
    }

    @Override
    public Door setHardness(float hardness) {
        return (Door) super.setHardness(hardness);
    }

    @Override
    public Door setUnbreakable() {
        return (Door) super.setUnbreakable();
    }

    @Override
    public Door setTicksRandomly(boolean ticksRandomly) {
        return (Door) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public Door setTranslationKey(String string) {
        return (Door) super.setTranslationKey(string);
    }

    @Override
    public Door disableStat() {
        return (Door) super.disableStat();
    }
}
