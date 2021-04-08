package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateBlockBase extends net.minecraft.block.BlockBase implements IBlockTemplate<TemplateBlockBase> {

    public TemplateBlockBase(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateBlockBase(Identifier identifier, int tex, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), tex, material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateBlockBase(int id, Material material) {
        super(id, material);
    }

    public TemplateBlockBase(int id, int tex, Material material) {
        super(id, tex, material);
    }

    @Override
    public TemplateBlockBase disableNotifyOnMetaDataChange() {
        return (TemplateBlockBase) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateBlockBase setSounds(BlockSounds sounds) {
        return (TemplateBlockBase) super.setSounds(sounds);
    }

    @Override
    public TemplateBlockBase setLightOpacity(int i) {
        return (TemplateBlockBase) super.setLightOpacity(i);
    }

    @Override
    public TemplateBlockBase setLightEmittance(float f) {
        return (TemplateBlockBase) super.setLightEmittance(f);
    }

    @Override
    public TemplateBlockBase setBlastResistance(float resistance) {
        return (TemplateBlockBase) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateBlockBase setHardness(float hardness) {
        return (TemplateBlockBase) super.setHardness(hardness);
    }

    @Override
    public TemplateBlockBase setUnbreakable() {
        return (TemplateBlockBase) super.setUnbreakable();
    }

    @Override
    public TemplateBlockBase setTicksRandomly(boolean ticksRandomly) {
        return (TemplateBlockBase) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateBlockBase setTranslationKey(String string) {
        return (TemplateBlockBase) super.setTranslationKey(string);
    }

    @Override
    public TemplateBlockBase disableStat() {
        return (TemplateBlockBase) super.disableStat();
    }
}
