package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTallGrass extends net.minecraft.block.TallGrass implements IBlockTemplate<TemplateTallGrass> {
    
    public TemplateTallGrass(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), j);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateTallGrass(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplateTallGrass disableNotifyOnMetaDataChange() {
        return (TemplateTallGrass) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateTallGrass setSounds(BlockSounds sounds) {
        return (TemplateTallGrass) super.setSounds(sounds);
    }

    @Override
    public TemplateTallGrass setLightOpacity(int i) {
        return (TemplateTallGrass) super.setLightOpacity(i);
    }

    @Override
    public TemplateTallGrass setLightEmittance(float f) {
        return (TemplateTallGrass) super.setLightEmittance(f);
    }

    @Override
    public TemplateTallGrass setBlastResistance(float resistance) {
        return (TemplateTallGrass) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateTallGrass setHardness(float hardness) {
        return (TemplateTallGrass) super.setHardness(hardness);
    }

    @Override
    public TemplateTallGrass setUnbreakable() {
        return (TemplateTallGrass) super.setUnbreakable();
    }

    @Override
    public TemplateTallGrass setTicksRandomly(boolean ticksRandomly) {
        return (TemplateTallGrass) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateTallGrass setTranslationKey(String string) {
        return (TemplateTallGrass) super.setTranslationKey(string);
    }

    @Override
    public TemplateTallGrass disableStat() {
        return (TemplateTallGrass) super.disableStat();
    }
}
