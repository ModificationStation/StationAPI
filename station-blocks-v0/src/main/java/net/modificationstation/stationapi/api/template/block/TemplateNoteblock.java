package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateNoteblock extends net.minecraft.block.Noteblock implements IBlockTemplate<TemplateNoteblock> {
    
    public TemplateNoteblock(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateNoteblock(int i) {
        super(i);
    }

    @Override
    public TemplateNoteblock disableNotifyOnMetaDataChange() {
        return (TemplateNoteblock) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateNoteblock setSounds(BlockSounds sounds) {
        return (TemplateNoteblock) super.setSounds(sounds);
    }

    @Override
    public TemplateNoteblock setLightOpacity(int i) {
        return (TemplateNoteblock) super.setLightOpacity(i);
    }

    @Override
    public TemplateNoteblock setLightEmittance(float f) {
        return (TemplateNoteblock) super.setLightEmittance(f);
    }

    @Override
    public TemplateNoteblock setBlastResistance(float resistance) {
        return (TemplateNoteblock) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateNoteblock setHardness(float hardness) {
        return (TemplateNoteblock) super.setHardness(hardness);
    }

    @Override
    public TemplateNoteblock setUnbreakable() {
        return (TemplateNoteblock) super.setUnbreakable();
    }

    @Override
    public TemplateNoteblock setTicksRandomly(boolean ticksRandomly) {
        return (TemplateNoteblock) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateNoteblock setTranslationKey(String string) {
        return (TemplateNoteblock) super.setTranslationKey(string);
    }

    @Override
    public TemplateNoteblock disableStat() {
        return (TemplateNoteblock) super.disableStat();
    }
}
