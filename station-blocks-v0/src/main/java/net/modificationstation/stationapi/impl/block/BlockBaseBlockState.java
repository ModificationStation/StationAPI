package net.modificationstation.stationapi.impl.block;

import net.minecraft.block.BlockBase;

public interface BlockBaseBlockState {

    StateManager<BlockBase, BlockState> getStateManager();

    BlockState getDefaultState();

    void appendProperties(StateManager.Builder<BlockBase, BlockState> builder);

    void setDefaultState(BlockState state);
}
