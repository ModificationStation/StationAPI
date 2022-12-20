package net.modificationstation.stationapi.api.vanillafix.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.jetbrains.annotations.Nullable;

public abstract class FixedBlockWithEntity extends TemplateBlockBase {

    public FixedBlockWithEntity(Identifier identifier, Material material) {
        super(identifier, material);
        HAS_TILE_ENTITY[id] = true;
    }

    @Override
    public void onBlockPlaced(Level world, int x, int y, int z, BlockState replacedState) {
        super.onBlockPlaced(world, x, y, z, replacedState);
        if (!replacedState.isOf(this)) {
            BlockPos pos = new BlockPos(x, y, z);
            world.setTileEntity(x, y, z, createBlockEntity(pos, ((BlockStateView) world).getBlockState(pos)));
        }
    }

    @Override
    public void onBlockRemoved(Level world, int x, int y, int z) {
        super.onBlockRemoved(world, x, y, z);
        if (!((BlockStateView) world).getBlockState(x, y, z).isOf(this))
            world.removeTileEntity(x, y, z);
    }

    @Nullable
    protected abstract TileEntityBase createBlockEntity(BlockPos pos, BlockState state);
}
