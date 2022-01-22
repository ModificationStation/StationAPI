package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCrops extends net.minecraft.block.Crops implements BlockTemplate<TemplateCrops> {

    public TemplateCrops(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplateCrops(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateCrops disableNotifyOnMetaDataChange() {
        return (TemplateCrops) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateCrops setSounds(BlockSounds sounds) {
        return (TemplateCrops) super.setSounds(sounds);
    }

    @Override
    public TemplateCrops setLightOpacity(int i) {
        return (TemplateCrops) super.setLightOpacity(i);
    }

    @Override
    public TemplateCrops setLightEmittance(float f) {
        return (TemplateCrops) super.setLightEmittance(f);
    }

    @Override
    public TemplateCrops setBlastResistance(float resistance) {
        return (TemplateCrops) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateCrops setHardness(float hardness) {
        return (TemplateCrops) super.setHardness(hardness);
    }

    @Override
    public TemplateCrops setUnbreakable() {
        return (TemplateCrops) super.setUnbreakable();
    }

    @Override
    public TemplateCrops setTicksRandomly(boolean ticksRandomly) {
        return (TemplateCrops) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateCrops setTranslationKey(String string) {
        return (TemplateCrops) super.setTranslationKey(string);
    }

    @Override
    public TemplateCrops disableStat() {
        return (TemplateCrops) super.disableStat();
    }
}
