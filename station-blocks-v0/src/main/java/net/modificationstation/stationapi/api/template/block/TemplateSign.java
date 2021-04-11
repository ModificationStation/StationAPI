package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSign extends net.minecraft.block.Sign implements IBlockTemplate<TemplateSign> {
    
    public TemplateSign(Identifier identifier, Class<? extends TileEntityBase> arg, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), arg, flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateSign(int i, Class<? extends TileEntityBase> arg, boolean flag) {
        super(i, arg, flag);
    }

    @Override
    public TemplateSign disableNotifyOnMetaDataChange() {
        return (TemplateSign) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateSign setSounds(BlockSounds sounds) {
        return (TemplateSign) super.setSounds(sounds);
    }

    @Override
    public TemplateSign setLightOpacity(int i) {
        return (TemplateSign) super.setLightOpacity(i);
    }

    @Override
    public TemplateSign setLightEmittance(float f) {
        return (TemplateSign) super.setLightEmittance(f);
    }

    @Override
    public TemplateSign setBlastResistance(float resistance) {
        return (TemplateSign) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateSign setHardness(float hardness) {
        return (TemplateSign) super.setHardness(hardness);
    }

    @Override
    public TemplateSign setUnbreakable() {
        return (TemplateSign) super.setUnbreakable();
    }

    @Override
    public TemplateSign setTicksRandomly(boolean ticksRandomly) {
        return (TemplateSign) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateSign setTranslationKey(String string) {
        return (TemplateSign) super.setTranslationKey(string);
    }

    @Override
    public TemplateSign disableStat() {
        return (TemplateSign) super.disableStat();
    }
}
