package net.modificationstation.stationapi.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSandstone extends net.minecraft.block.Sandstone implements IBlockTemplate<TemplateSandstone> {
    
    public TemplateSandstone(Identifier identifier) {
        this(BlockRegistry.INSTANCE.getNextSerializedID());
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateSandstone(int i) {
        super(i);
    }

    @Override
    public TemplateSandstone disableNotifyOnMetaDataChange() {
        return (TemplateSandstone) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateSandstone setSounds(BlockSounds sounds) {
        return (TemplateSandstone) super.setSounds(sounds);
    }

    @Override
    public TemplateSandstone setLightOpacity(int i) {
        return (TemplateSandstone) super.setLightOpacity(i);
    }

    @Override
    public TemplateSandstone setLightEmittance(float f) {
        return (TemplateSandstone) super.setLightEmittance(f);
    }

    @Override
    public TemplateSandstone setBlastResistance(float resistance) {
        return (TemplateSandstone) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateSandstone setHardness(float hardness) {
        return (TemplateSandstone) super.setHardness(hardness);
    }

    @Override
    public TemplateSandstone setUnbreakable() {
        return (TemplateSandstone) super.setUnbreakable();
    }

    @Override
    public TemplateSandstone setTicksRandomly(boolean ticksRandomly) {
        return (TemplateSandstone) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateSandstone setTranslationKey(String string) {
        return (TemplateSandstone) super.setTranslationKey(string);
    }

    @Override
    public TemplateSandstone disableStat() {
        return (TemplateSandstone) super.disableStat();
    }
}
