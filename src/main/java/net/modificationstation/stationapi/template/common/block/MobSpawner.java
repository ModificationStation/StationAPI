package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class MobSpawner extends net.minecraft.block.MobSpawner implements IBlockTemplate<MobSpawner> {

    public MobSpawner(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public MobSpawner(int i, int j) {
        super(i, j);
    }

    @Override
    public MobSpawner disableNotifyOnMetaDataChange() {
        return (MobSpawner) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public MobSpawner setSounds(BlockSounds sounds) {
        return (MobSpawner) super.setSounds(sounds);
    }

    @Override
    public MobSpawner setLightOpacity(int i) {
        return (MobSpawner) super.setLightOpacity(i);
    }

    @Override
    public MobSpawner setLightEmittance(float f) {
        return (MobSpawner) super.setLightEmittance(f);
    }

    @Override
    public MobSpawner setBlastResistance(float resistance) {
        return (MobSpawner) super.setBlastResistance(resistance);
    }

    @Override
    public MobSpawner setHardness(float hardness) {
        return (MobSpawner) super.setHardness(hardness);
    }

    @Override
    public MobSpawner setUnbreakable() {
        return (MobSpawner) super.setUnbreakable();
    }

    @Override
    public MobSpawner setTicksRandomly(boolean ticksRandomly) {
        return (MobSpawner) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public MobSpawner setTranslationKey(String string) {
        return (MobSpawner) super.setTranslationKey(string);
    }

    @Override
    public MobSpawner disableStat() {
        return (MobSpawner) super.disableStat();
    }
}
