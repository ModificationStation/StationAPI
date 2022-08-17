package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSapling extends net.minecraft.block.Sapling implements BlockTemplate<TemplateSapling> {

    public TemplateSapling(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSapling(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateSapling disableNotifyOnMetaDataChange() {
        return (TemplateSapling) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateSapling setSounds(BlockSounds sounds) {
        return (TemplateSapling) super.setSounds(sounds);
    }

    @Override
    public TemplateSapling setLightOpacity(int i) {
        return (TemplateSapling) super.setLightOpacity(i);
    }

    @Override
    public TemplateSapling setLightEmittance(float f) {
        return (TemplateSapling) super.setLightEmittance(f);
    }

    @Override
    public TemplateSapling setBlastResistance(float resistance) {
        return (TemplateSapling) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateSapling setHardness(float hardness) {
        return (TemplateSapling) super.setHardness(hardness);
    }

    @Override
    public TemplateSapling setUnbreakable() {
        return (TemplateSapling) super.setUnbreakable();
    }

    @Override
    public TemplateSapling setTicksRandomly(boolean ticksRandomly) {
        return (TemplateSapling) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateSapling setTranslationKey(String string) {
        return (TemplateSapling) super.setTranslationKey(string);
    }

    @Override
    public TemplateSapling disableStat() {
        return (TemplateSapling) super.disableStat();
    }
}
