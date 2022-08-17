package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateChest extends net.minecraft.block.Chest implements BlockTemplate<TemplateChest> {

    public TemplateChest(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextLegacyId());
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateChest(int id) {
        super(id);
    }

    @Override
    public TemplateChest disableNotifyOnMetaDataChange() {
        return (TemplateChest) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateChest setSounds(BlockSounds sounds) {
        return (TemplateChest) super.setSounds(sounds);
    }

    @Override
    public TemplateChest setLightOpacity(int i) {
        return (TemplateChest) super.setLightOpacity(i);
    }

    @Override
    public TemplateChest setLightEmittance(float f) {
        return (TemplateChest) super.setLightEmittance(f);
    }

    @Override
    public TemplateChest setBlastResistance(float resistance) {
        return (TemplateChest) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateChest setHardness(float hardness) {
        return (TemplateChest) super.setHardness(hardness);
    }

    @Override
    public TemplateChest setUnbreakable() {
        return (TemplateChest) super.setUnbreakable();
    }

    @Override
    public TemplateChest setTicksRandomly(boolean ticksRandomly) {
        return (TemplateChest) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateChest setTranslationKey(String string) {
        return (TemplateChest) super.setTranslationKey(string);
    }

    @Override
    public TemplateChest disableStat() {
        return (TemplateChest) super.disableStat();
    }
}
