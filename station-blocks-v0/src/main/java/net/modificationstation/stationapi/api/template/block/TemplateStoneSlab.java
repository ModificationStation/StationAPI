package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateStoneSlab extends net.minecraft.block.StoneSlab implements IBlockTemplate<TemplateStoneSlab> {
    
    public TemplateStoneSlab(Identifier identifier, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), flag);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }
    
    public TemplateStoneSlab(int i, boolean flag) {
        super(i, flag);
    }

    @Override
    public TemplateStoneSlab disableNotifyOnMetaDataChange() {
        return (TemplateStoneSlab) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateStoneSlab setSounds(BlockSounds sounds) {
        return (TemplateStoneSlab) super.setSounds(sounds);
    }

    @Override
    public TemplateStoneSlab setLightOpacity(int i) {
        return (TemplateStoneSlab) super.setLightOpacity(i);
    }

    @Override
    public TemplateStoneSlab setLightEmittance(float f) {
        return (TemplateStoneSlab) super.setLightEmittance(f);
    }

    @Override
    public TemplateStoneSlab setBlastResistance(float resistance) {
        return (TemplateStoneSlab) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateStoneSlab setHardness(float hardness) {
        return (TemplateStoneSlab) super.setHardness(hardness);
    }

    @Override
    public TemplateStoneSlab setUnbreakable() {
        return (TemplateStoneSlab) super.setUnbreakable();
    }

    @Override
    public TemplateStoneSlab setTicksRandomly(boolean ticksRandomly) {
        return (TemplateStoneSlab) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateStoneSlab setTranslationKey(String string) {
        return (TemplateStoneSlab) super.setTranslationKey(string);
    }

    @Override
    public TemplateStoneSlab disableStat() {
        return (TemplateStoneSlab) super.disableStat();
    }
}
