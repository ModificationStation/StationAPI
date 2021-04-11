package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateGlass extends net.minecraft.block.Glass implements IBlockTemplate<TemplateGlass> {

    public TemplateGlass(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j, arg, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateGlass(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }

    @Override
    public TemplateGlass disableNotifyOnMetaDataChange() {
        return (TemplateGlass) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateGlass setSounds(BlockSounds sounds) {
        return (TemplateGlass) super.setSounds(sounds);
    }

    @Override
    public TemplateGlass setLightOpacity(int i) {
        return (TemplateGlass) super.setLightOpacity(i);
    }

    @Override
    public TemplateGlass setLightEmittance(float f) {
        return (TemplateGlass) super.setLightEmittance(f);
    }

    @Override
    public TemplateGlass setBlastResistance(float resistance) {
        return (TemplateGlass) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateGlass setHardness(float hardness) {
        return (TemplateGlass) super.setHardness(hardness);
    }

    @Override
    public TemplateGlass setUnbreakable() {
        return (TemplateGlass) super.setUnbreakable();
    }

    @Override
    public TemplateGlass setTicksRandomly(boolean ticksRandomly) {
        return (TemplateGlass) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateGlass setTranslationKey(String string) {
        return (TemplateGlass) super.setTranslationKey(string);
    }

    @Override
    public TemplateGlass disableStat() {
        return (TemplateGlass) super.disableStat();
    }
}
