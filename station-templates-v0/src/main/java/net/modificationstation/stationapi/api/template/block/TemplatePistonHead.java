package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePistonHead extends net.minecraft.block.PistonHead implements BlockTemplate<TemplatePistonHead> {

    public TemplatePistonHead(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextSerialID(), j);
        BlockRegistry.INSTANCE.register(identifier, this);
    }

    public TemplatePistonHead(int i, int j) {
        super(i, j);
    }

    @Override
    public TemplatePistonHead disableNotifyOnMetaDataChange() {
        return (TemplatePistonHead) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplatePistonHead setSounds(BlockSounds sounds) {
        return (TemplatePistonHead) super.setSounds(sounds);
    }

    @Override
    public TemplatePistonHead setLightOpacity(int i) {
        return (TemplatePistonHead) super.setLightOpacity(i);
    }

    @Override
    public TemplatePistonHead setLightEmittance(float f) {
        return (TemplatePistonHead) super.setLightEmittance(f);
    }

    @Override
    public TemplatePistonHead setBlastResistance(float resistance) {
        return (TemplatePistonHead) super.setBlastResistance(resistance);
    }

    @Override
    public TemplatePistonHead setHardness(float hardness) {
        return (TemplatePistonHead) super.setHardness(hardness);
    }

    @Override
    public TemplatePistonHead setUnbreakable() {
        return (TemplatePistonHead) super.setUnbreakable();
    }

    @Override
    public TemplatePistonHead setTicksRandomly(boolean ticksRandomly) {
        return (TemplatePistonHead) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplatePistonHead setTranslationKey(String string) {
        return (TemplatePistonHead) super.setTranslationKey(string);
    }

    @Override
    public TemplatePistonHead disableStat() {
        return (TemplatePistonHead) super.disableStat();
    }

    @Override
    public Atlas getAtlas() {
        return BlockTemplate.super.getAtlas();
    }
}
