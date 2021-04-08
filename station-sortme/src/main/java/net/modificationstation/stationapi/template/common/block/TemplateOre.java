package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateOre extends net.minecraft.block.Ore implements IBlockTemplate<TemplateOre> {

    public TemplateOre(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateOre(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateOre disableNotifyOnMetaDataChange() {
        return (TemplateOre) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateOre setSounds(BlockSounds sounds) {
        return (TemplateOre) super.setSounds(sounds);
    }

    @Override
    public TemplateOre setLightOpacity(int i) {
        return (TemplateOre) super.setLightOpacity(i);
    }

    @Override
    public TemplateOre setLightEmittance(float f) {
        return (TemplateOre) super.setLightEmittance(f);
    }

    @Override
    public TemplateOre setBlastResistance(float resistance) {
        return (TemplateOre) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateOre setHardness(float hardness) {
        return (TemplateOre) super.setHardness(hardness);
    }

    @Override
    public TemplateOre setUnbreakable() {
        return (TemplateOre) super.setUnbreakable();
    }

    @Override
    public TemplateOre setTicksRandomly(boolean ticksRandomly) {
        return (TemplateOre) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateOre setTranslationKey(String string) {
        return (TemplateOre) super.setTranslationKey(string);
    }

    @Override
    public TemplateOre disableStat() {
        return (TemplateOre) super.disableStat();
    }
}
