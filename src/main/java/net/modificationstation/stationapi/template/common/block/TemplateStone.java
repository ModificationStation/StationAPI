package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplateStone extends net.minecraft.block.Stone implements IBlockTemplate<TemplateStone> {
    
    public TemplateStone(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateStone(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateStone disableNotifyOnMetaDataChange() {
        return (TemplateStone) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateStone setSounds(BlockSounds sounds) {
        return (TemplateStone) super.setSounds(sounds);
    }

    @Override
    public TemplateStone setLightOpacity(int i) {
        return (TemplateStone) super.setLightOpacity(i);
    }

    @Override
    public TemplateStone setLightEmittance(float f) {
        return (TemplateStone) super.setLightEmittance(f);
    }

    @Override
    public TemplateStone setBlastResistance(float resistance) {
        return (TemplateStone) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateStone setHardness(float hardness) {
        return (TemplateStone) super.setHardness(hardness);
    }

    @Override
    public TemplateStone setUnbreakable() {
        return (TemplateStone) super.setUnbreakable();
    }

    @Override
    public TemplateStone setTicksRandomly(boolean ticksRandomly) {
        return (TemplateStone) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateStone setTranslationKey(String string) {
        return (TemplateStone) super.setTranslationKey(string);
    }

    @Override
    public TemplateStone disableStat() {
        return (TemplateStone) super.disableStat();
    }
}
