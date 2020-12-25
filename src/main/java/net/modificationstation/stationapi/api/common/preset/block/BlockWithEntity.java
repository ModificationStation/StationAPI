package net.modificationstation.stationapi.api.common.preset.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public abstract class BlockWithEntity extends net.minecraft.block.BlockWithEntity {

    public BlockWithEntity(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public BlockWithEntity(Identifier identifier, int tex, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), tex, material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public BlockWithEntity(int i, Material arg) {
        super(i, arg);
    }

    public BlockWithEntity(int i, int j, Material arg) {
        super(i, j, arg);
    }

    @Override
    public BlockWithEntity disableNotifyOnMetaDataChange() {
        return (BlockWithEntity) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public BlockWithEntity sounds(BlockSounds sounds) {
        return (BlockWithEntity) super.sounds(sounds);
    }

    @Override
    public BlockWithEntity setLightOpacity(int i) {
        return (BlockWithEntity) super.setLightOpacity(i);
    }

    @Override
    public BlockWithEntity setLightEmittance(float f) {
        return (BlockWithEntity) super.setLightEmittance(f);
    }

    @Override
    public BlockWithEntity setBlastResistance(float resistance) {
        return (BlockWithEntity) super.setBlastResistance(resistance);
    }

    @Override
    public BlockWithEntity setHardness(float hardness) {
        return (BlockWithEntity) super.setHardness(hardness);
    }

    @Override
    public BlockWithEntity setUnbreakable() {
        return (BlockWithEntity) super.setUnbreakable();
    }

    @Override
    public BlockWithEntity setTicksRandomly(boolean ticksRandomly) {
        return (BlockWithEntity) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public BlockWithEntity setName(String string) {
        return (BlockWithEntity) super.setName(string);
    }

    @Override
    public BlockWithEntity disableStat() {
        return (BlockWithEntity) super.disableStat();
    }
}
