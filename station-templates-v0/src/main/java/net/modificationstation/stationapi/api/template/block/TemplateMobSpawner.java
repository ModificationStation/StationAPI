package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMobSpawner extends net.minecraft.block.MobSpawner implements BlockTemplate<TemplateMobSpawner> {

    public TemplateMobSpawner(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateMobSpawner(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateMobSpawner disableNotifyOnMetaDataChange() {
        return (TemplateMobSpawner) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateMobSpawner setSounds(BlockSounds sounds) {
        return (TemplateMobSpawner) super.setSounds(sounds);
    }

    @Override
    public TemplateMobSpawner setLightOpacity(int i) {
        return (TemplateMobSpawner) super.setLightOpacity(i);
    }

    @Override
    public TemplateMobSpawner setLightEmittance(float f) {
        return (TemplateMobSpawner) super.setLightEmittance(f);
    }

    @Override
    public TemplateMobSpawner setBlastResistance(float resistance) {
        return (TemplateMobSpawner) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateMobSpawner setHardness(float hardness) {
        return (TemplateMobSpawner) super.setHardness(hardness);
    }

    @Override
    public TemplateMobSpawner setUnbreakable() {
        return (TemplateMobSpawner) super.setUnbreakable();
    }

    @Override
    public TemplateMobSpawner setTicksRandomly(boolean ticksRandomly) {
        return (TemplateMobSpawner) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateMobSpawner setTranslationKey(String string) {
        return (TemplateMobSpawner) super.setTranslationKey(string);
    }

    @Override
    public TemplateMobSpawner disableStat() {
        return (TemplateMobSpawner) super.disableStat();
    }
}
