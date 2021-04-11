package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePumpkin extends net.minecraft.block.Pumpkin implements IBlockTemplate<TemplatePumpkin> {

    public TemplatePumpkin(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplatePumpkin(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public TemplatePumpkin disableNotifyOnMetaDataChange() {
        return (TemplatePumpkin) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplatePumpkin setSounds(BlockSounds sounds) {
        return (TemplatePumpkin) super.setSounds(sounds);
    }

    @Override
    public TemplatePumpkin setLightOpacity(int i) {
        return (TemplatePumpkin) super.setLightOpacity(i);
    }

    @Override
    public TemplatePumpkin setLightEmittance(float f) {
        return (TemplatePumpkin) super.setLightEmittance(f);
    }

    @Override
    public TemplatePumpkin setBlastResistance(float resistance) {
        return (TemplatePumpkin) super.setBlastResistance(resistance);
    }

    @Override
    public TemplatePumpkin setHardness(float hardness) {
        return (TemplatePumpkin) super.setHardness(hardness);
    }

    @Override
    public TemplatePumpkin setUnbreakable() {
        return (TemplatePumpkin) super.setUnbreakable();
    }

    @Override
    public TemplatePumpkin setTicksRandomly(boolean ticksRandomly) {
        return (TemplatePumpkin) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplatePumpkin setTranslationKey(String string) {
        return (TemplatePumpkin) super.setTranslationKey(string);
    }

    @Override
    public TemplatePumpkin disableStat() {
        return (TemplatePumpkin) super.disableStat();
    }
}
