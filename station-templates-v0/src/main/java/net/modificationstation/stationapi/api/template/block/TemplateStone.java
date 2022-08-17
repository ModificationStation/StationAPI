package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStone extends net.minecraft.block.Stone implements BlockTemplate<TemplateStone> {
    
    public TemplateStone(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j);
        BlockTemplate.onConstructor(this, identifier);
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
