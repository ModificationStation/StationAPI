package net.modificationstation.stationapi.template.common.block;

import net.minecraft.block.BlockSounds;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class BlockBase extends net.minecraft.block.BlockBase implements IBlockTemplate<BlockBase> {

    public BlockBase(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public BlockBase(Identifier identifier, int tex, Material material) {
        this(BlockRegistry.INSTANCE.getNextSerializedID(), tex, material);
        BlockRegistry.INSTANCE.registerValue(identifier, this);
    }

    public BlockBase(int id, Material material) {
        super(id, material);
    }

    public BlockBase(int id, int tex, Material material) {
        super(id, tex, material);
    }

    @Override
    public BlockBase disableNotifyOnMetaDataChange() {
        return (BlockBase) super.disableNotifyOnMetaDataChange();
    }

    @Override
    public BlockBase setSounds(BlockSounds sounds) {
        return (BlockBase) super.setSounds(sounds);
    }

    @Override
    public BlockBase setLightOpacity(int i) {
        return (BlockBase) super.setLightOpacity(i);
    }

    @Override
    public BlockBase setLightEmittance(float f) {
        return (BlockBase) super.setLightEmittance(f);
    }

    @Override
    public BlockBase setBlastResistance(float resistance) {
        return (BlockBase) super.setBlastResistance(resistance);
    }

    @Override
    public BlockBase setHardness(float hardness) {
        return (BlockBase) super.setHardness(hardness);
    }

    @Override
    public BlockBase setUnbreakable() {
        return (BlockBase) super.setUnbreakable();
    }

    @Override
    public BlockBase setTicksRandomly(boolean ticksRandomly) {
        return (BlockBase) super.setTicksRandomly(ticksRandomly);
    }

    @Override
    public BlockBase setTranslationKey(String string) {
        return (BlockBase) super.setTranslationKey(string);
    }

    @Override
    public BlockBase disableStat() {
        return (BlockBase) super.disableStat();
    }
}
