package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSnowBlock extends net.minecraft.block.SnowBlock implements IBlockTemplate<TemplateSnowBlock> {
    
    public TemplateSnowBlock(Identifier identifier, int texUVStart) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), texUVStart);
        BlockRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateSnowBlock(int id, int texUVStart) {
        super(id, texUVStart);
    }

    @Override
    public TemplateSnowBlock disableNotifyOnMetaDataChange() {
        return (TemplateSnowBlock) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateSnowBlock setSounds(BlockSounds sounds) {
        return (TemplateSnowBlock) super.setSounds(sounds);
    }

    @Override
    public TemplateSnowBlock setLightOpacity(int i) {
        return (TemplateSnowBlock) super.setLightOpacity(i);
    }

    @Override
    public TemplateSnowBlock setLightEmittance(float f) {
        return (TemplateSnowBlock) super.setLightEmittance(f);
    }

    @Override
    public TemplateSnowBlock setBlastResistance(float resistance) {
        return (TemplateSnowBlock) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateSnowBlock setHardness(float hardness) {
        return (TemplateSnowBlock) super.setHardness(hardness);
    }

    @Override
    public TemplateSnowBlock setUnbreakable() {
        return (TemplateSnowBlock) super.setUnbreakable();
    }

    @Override
    public TemplateSnowBlock setTicksRandomly(boolean ticksRandomly) {
        return (TemplateSnowBlock) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateSnowBlock setTranslationKey(String string) {
        return (TemplateSnowBlock) super.setTranslationKey(string);
    }

    @Override
    public TemplateSnowBlock disableStat() {
        return (TemplateSnowBlock) super.disableStat();
    }
}
