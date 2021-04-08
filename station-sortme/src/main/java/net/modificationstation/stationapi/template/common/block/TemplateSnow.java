package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSnow extends net.minecraft.block.Snow implements IBlockTemplate<TemplateSnow> {
    
    public TemplateSnow(Identifier identifier, int texUVStart) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texUVStart);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateSnow(int id, int texUVStart) {
        super(id, texUVStart);
    }

    @Override
    public TemplateSnow disableNotifyOnMetaDataChange() {
        return (TemplateSnow) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateSnow setSounds(BlockSounds sounds) {
        return (TemplateSnow) super.setSounds(sounds);
    }

    @Override
    public TemplateSnow setLightOpacity(int i) {
        return (TemplateSnow) super.setLightOpacity(i);
    }

    @Override
    public TemplateSnow setLightEmittance(float f) {
        return (TemplateSnow) super.setLightEmittance(f);
    }

    @Override
    public TemplateSnow setBlastResistance(float resistance) {
        return (TemplateSnow) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateSnow setHardness(float hardness) {
        return (TemplateSnow) super.setHardness(hardness);
    }

    @Override
    public TemplateSnow setUnbreakable() {
        return (TemplateSnow) super.setUnbreakable();
    }

    @Override
    public TemplateSnow setTicksRandomly(boolean ticksRandomly) {
        return (TemplateSnow) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateSnow setTranslationKey(String string) {
        return (TemplateSnow) super.setTranslationKey(string);
    }

    @Override
    public TemplateSnow disableStat() {
        return (TemplateSnow) super.disableStat();
    }
}
