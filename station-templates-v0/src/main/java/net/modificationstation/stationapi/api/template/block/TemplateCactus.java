package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateCactus extends net.minecraft.block.Cactus implements BlockTemplate<TemplateCactus> {

    public TemplateCactus(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCactus(int id, int texture) {
        super(id, texture);
    }

    @Override
    public TemplateCactus disableNotifyOnMetaDataChange() {
        return (TemplateCactus) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateCactus setSounds(BlockSounds sounds) {
        return (TemplateCactus) super.setSounds(sounds);
    }

    @Override
    public TemplateCactus setLightOpacity(int i) {
        return (TemplateCactus) super.setLightOpacity(i);
    }

    @Override
    public TemplateCactus setLightEmittance(float f) {
        return (TemplateCactus) super.setLightEmittance(f);
    }

    @Override
    public TemplateCactus setBlastResistance(float resistance) {
        return (TemplateCactus) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateCactus setHardness(float hardness) {
        return (TemplateCactus) super.setHardness(hardness);
    }

    @Override
    public TemplateCactus setUnbreakable() {
        return (TemplateCactus) super.setUnbreakable();
    }

    @Override
    public TemplateCactus setTicksRandomly(boolean ticksRandomly) {
        return (TemplateCactus) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateCactus setTranslationKey(String string) {
        return (TemplateCactus) super.setTranslationKey(string);
    }

    @Override
    public TemplateCactus disableStat() {
        return (TemplateCactus) super.disableStat();
    }
}
