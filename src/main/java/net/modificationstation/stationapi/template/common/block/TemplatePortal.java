package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class TemplatePortal extends net.minecraft.block.Portal implements IBlockTemplate<TemplatePortal> {

    public TemplatePortal(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplatePortal(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplatePortal disableNotifyOnMetaDataChange() {
        return (TemplatePortal) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplatePortal setSounds(BlockSounds sounds) {
        return (TemplatePortal) super.setSounds(sounds);
    }

    @Override
    public TemplatePortal setLightOpacity(int i) {
        return (TemplatePortal) super.setLightOpacity(i);
    }

    @Override
    public TemplatePortal setLightEmittance(float f) {
        return (TemplatePortal) super.setLightEmittance(f);
    }

    @Override
    public TemplatePortal setBlastResistance(float resistance) {
        return (TemplatePortal) super.setBlastResistance(resistance);
    }

    @Override
    public TemplatePortal setHardness(float hardness) {
        return (TemplatePortal) super.setHardness(hardness);
    }

    @Override
    public TemplatePortal setUnbreakable() {
        return (TemplatePortal) super.setUnbreakable();
    }

    @Override
    public TemplatePortal setTicksRandomly(boolean ticksRandomly) {
        return (TemplatePortal) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplatePortal setTranslationKey(String string) {
        return (TemplatePortal) super.setTranslationKey(string);
    }

    @Override
    public TemplatePortal disableStat() {
        return (TemplatePortal) super.disableStat();
    }
}
