package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePiston extends net.minecraft.block.Piston implements IBlockTemplate<TemplatePiston> {

    public TemplatePiston(Identifier identifier, int j, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplatePiston(int i, int j, boolean flag) {
        super(i, j, flag);
    }

    @Override
    public TemplatePiston disableNotifyOnMetaDataChange() {
        return (TemplatePiston) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplatePiston setSounds(BlockSounds sounds) {
        return (TemplatePiston) super.setSounds(sounds);
    }

    @Override
    public TemplatePiston setLightOpacity(int i) {
        return (TemplatePiston) super.setLightOpacity(i);
    }

    @Override
    public TemplatePiston setLightEmittance(float f) {
        return (TemplatePiston) super.setLightEmittance(f);
    }

    @Override
    public TemplatePiston setBlastResistance(float resistance) {
        return (TemplatePiston) super.setBlastResistance(resistance);
    }

    @Override
    public TemplatePiston setHardness(float hardness) {
        return (TemplatePiston) super.setHardness(hardness);
    }

    @Override
    public TemplatePiston setUnbreakable() {
        return (TemplatePiston) super.setUnbreakable();
    }

    @Override
    public TemplatePiston setTicksRandomly(boolean ticksRandomly) {
        return (TemplatePiston) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplatePiston setTranslationKey(String string) {
        return (TemplatePiston) super.setTranslationKey(string);
    }

    @Override
    public TemplatePiston disableStat() {
        return (TemplatePiston) super.disableStat();
    }
}
