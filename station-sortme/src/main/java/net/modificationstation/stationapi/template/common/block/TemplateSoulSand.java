package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSoulSand extends net.minecraft.block.SoulSand implements IBlockTemplate<TemplateSoulSand> {
    
    public TemplateSoulSand(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateSoulSand(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateSoulSand disableNotifyOnMetaDataChange() {
        return (TemplateSoulSand) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateSoulSand setSounds(BlockSounds sounds) {
        return (TemplateSoulSand) super.setSounds(sounds);
    }

    @Override
    public TemplateSoulSand setLightOpacity(int i) {
        return (TemplateSoulSand) super.setLightOpacity(i);
    }

    @Override
    public TemplateSoulSand setLightEmittance(float f) {
        return (TemplateSoulSand) super.setLightEmittance(f);
    }

    @Override
    public TemplateSoulSand setBlastResistance(float resistance) {
        return (TemplateSoulSand) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateSoulSand setHardness(float hardness) {
        return (TemplateSoulSand) super.setHardness(hardness);
    }

    @Override
    public TemplateSoulSand setUnbreakable() {
        return (TemplateSoulSand) super.setUnbreakable();
    }

    @Override
    public TemplateSoulSand setTicksRandomly(boolean ticksRandomly) {
        return (TemplateSoulSand) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateSoulSand setTranslationKey(String string) {
        return (TemplateSoulSand) super.setTranslationKey(string);
    }

    @Override
    public TemplateSoulSand disableStat() {
        return (TemplateSoulSand) super.disableStat();
    }
}
