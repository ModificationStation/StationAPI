package net.modificationstation.stationapi.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBookshelf extends net.minecraft.block.Bookshelf implements IBlockTemplate<TemplateBookshelf> {

    public TemplateBookshelf(Identifier identifier, int texture) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), texture);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateBookshelf(int id, int texture) {
        super(id, texture);
    }

    @Override
    public TemplateBookshelf disableNotifyOnMetaDataChange() {
        return (TemplateBookshelf) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateBookshelf setSounds(BlockSounds sounds) {
        return (TemplateBookshelf) super.setSounds(sounds);
    }

    @Override
    public TemplateBookshelf setLightOpacity(int i) {
        return (TemplateBookshelf) super.setLightOpacity(i);
    }

    @Override
    public TemplateBookshelf setLightEmittance(float f) {
        return (TemplateBookshelf) super.setLightEmittance(f);
    }

    @Override
    public TemplateBookshelf setBlastResistance(float resistance) {
        return (TemplateBookshelf) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateBookshelf setHardness(float hardness) {
        return (TemplateBookshelf) super.setHardness(hardness);
    }

    @Override
    public TemplateBookshelf setUnbreakable() {
        return (TemplateBookshelf) super.setUnbreakable();
    }

    @Override
    public TemplateBookshelf setTicksRandomly(boolean ticksRandomly) {
        return (TemplateBookshelf) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateBookshelf setTranslationKey(String string) {
        return (TemplateBookshelf) super.setTranslationKey(string);
    }

    @Override
    public TemplateBookshelf disableStat() {
        return (TemplateBookshelf) super.disableStat();
    }
}
