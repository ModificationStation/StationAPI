package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public abstract class TemplateBlockWithEntity extends net.minecraft.block.BlockWithEntity implements IBlockTemplate<TemplateBlockWithEntity> {

    public TemplateBlockWithEntity(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateBlockWithEntity(Identifier identifier, int tex, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), tex, material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public TemplateBlockWithEntity(int i, Material arg) {
        super(i, arg);
    }

    public TemplateBlockWithEntity(int i, int j, Material arg) {
        super(i, j, arg);
    }

    @Override
    public TemplateBlockWithEntity disableNotifyOnMetaDataChange() {
        return (TemplateBlockWithEntity) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public TemplateBlockWithEntity setSounds(BlockSounds sounds) {
        return (TemplateBlockWithEntity) super.setSounds(sounds);
    }

    @Override
    public TemplateBlockWithEntity setLightOpacity(int i) {
        return (TemplateBlockWithEntity) super.setLightOpacity(i);
    }

    @Override
    public TemplateBlockWithEntity setLightEmittance(float f) {
        return (TemplateBlockWithEntity) super.setLightEmittance(f);
    }

    @Override
    public TemplateBlockWithEntity setBlastResistance(float resistance) {
        return (TemplateBlockWithEntity) super.setBlastResistance(resistance);
    }

    @Override
    public TemplateBlockWithEntity setHardness(float hardness) {
        return (TemplateBlockWithEntity) super.setHardness(hardness);
    }

    @Override
    public TemplateBlockWithEntity setUnbreakable() {
        return (TemplateBlockWithEntity) super.setUnbreakable();
    }

    @Override
    public TemplateBlockWithEntity setTicksRandomly(boolean ticksRandomly) {
        return (TemplateBlockWithEntity) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public TemplateBlockWithEntity setTranslationKey(String string) {
        return (TemplateBlockWithEntity) super.setTranslationKey(string);
    }

    @Override
    public TemplateBlockWithEntity disableStat() {
        return (TemplateBlockWithEntity) super.disableStat();
    }
}
