package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateButton extends net.minecraft.block.Button implements IBlockTemplate<TemplateButton> {

    public TemplateButton(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateButton(int id, int texture) {
        super(id, texture);
    }

    @Override
    public TemplateButton disableNotifyOnMetaDataChange() {
        return (TemplateButton) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateButton setSounds(BlockSounds sounds) {
        return (TemplateButton) super.setSounds(sounds);
    }

    @Override
    public TemplateButton setLightOpacity(int i) {
        return (TemplateButton) super.setLightOpacity(i);
    }

    @Override
    public TemplateButton setLightEmittance(float f) {
        return (TemplateButton) super.setLightEmittance(f);
    }

    @Override
    public TemplateButton setBlastResistance(float resistance) {
        return (TemplateButton) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateButton setHardness(float hardness) {
        return (TemplateButton) super.setHardness(hardness);
    }

    @Override
    public TemplateButton setUnbreakable() {
        return (TemplateButton) super.setUnbreakable();
    }

    @Override
    public TemplateButton setTicksRandomly(boolean ticksRandomly) {
        return (TemplateButton) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateButton setTranslationKey(String string) {
        return (TemplateButton) super.setTranslationKey(string);
    }

    @Override
    public TemplateButton disableStat() {
        return (TemplateButton) super.disableStat();
    }
}
