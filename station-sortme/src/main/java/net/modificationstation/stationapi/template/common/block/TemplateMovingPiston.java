package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMovingPiston extends net.minecraft.block.MovingPiston implements IBlockTemplate<TemplateMovingPiston> {
    
    public TemplateMovingPiston(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateMovingPiston(int id) {
        super(id);
    }

    @Override
    public TemplateMovingPiston disableNotifyOnMetaDataChange() {
        return (TemplateMovingPiston) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateMovingPiston setSounds(BlockSounds sounds) {
        return (TemplateMovingPiston) super.setSounds(sounds);
    }

    @Override
    public TemplateMovingPiston setLightOpacity(int i) {
        return (TemplateMovingPiston) super.setLightOpacity(i);
    }

    @Override
    public TemplateMovingPiston setLightEmittance(float f) {
        return (TemplateMovingPiston) super.setLightEmittance(f);
    }

    @Override
    public TemplateMovingPiston setBlastResistance(float resistance) {
        return (TemplateMovingPiston) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateMovingPiston setHardness(float hardness) {
        return (TemplateMovingPiston) super.setHardness(hardness);
    }

    @Override
    public TemplateMovingPiston setUnbreakable() {
        return (TemplateMovingPiston) super.setUnbreakable();
    }

    @Override
    public TemplateMovingPiston setTicksRandomly(boolean ticksRandomly) {
        return (TemplateMovingPiston) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateMovingPiston setTranslationKey(String string) {
        return (TemplateMovingPiston) super.setTranslationKey(string);
    }

    @Override
    public TemplateMovingPiston disableStat() {
        return (TemplateMovingPiston) super.disableStat();
    }
}
