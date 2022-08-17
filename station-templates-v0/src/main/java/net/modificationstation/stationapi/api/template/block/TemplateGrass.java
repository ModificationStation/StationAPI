package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateGrass extends net.minecraft.block.Grass implements BlockTemplate<TemplateGrass> {

    public TemplateGrass(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateGrass(int id) {
        super(id);
    }

    @Override
    public TemplateGrass disableNotifyOnMetaDataChange() {
        return (TemplateGrass) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateGrass setSounds(BlockSounds sounds) {
        return (TemplateGrass) super.setSounds(sounds);
    }

    @Override
    public TemplateGrass setLightOpacity(int i) {
        return (TemplateGrass) super.setLightOpacity(i);
    }

    @Override
    public TemplateGrass setLightEmittance(float f) {
        return (TemplateGrass) super.setLightEmittance(f);
    }

    @Override
    public TemplateGrass setBlastResistance(float resistance) {
        return (TemplateGrass) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateGrass setHardness(float hardness) {
        return (TemplateGrass) super.setHardness(hardness);
    }

    @Override
    public TemplateGrass setUnbreakable() {
        return (TemplateGrass) super.setUnbreakable();
    }

    @Override
    public TemplateGrass setTicksRandomly(boolean ticksRandomly) {
        return (TemplateGrass) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateGrass setTranslationKey(String string) {
        return (TemplateGrass) super.setTranslationKey(string);
    }

    @Override
    public TemplateGrass disableStat() {
        return (TemplateGrass) super.disableStat();
    }
}
