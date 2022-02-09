package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;

public interface BlockStateHolder {

    StateManager<BlockBase, BlockState> getStateManager();

    BlockState getDefaultState();

    void appendProperties(StateManager.Builder<BlockBase, BlockState> builder);

    void setDefaultState(BlockState state);
}
