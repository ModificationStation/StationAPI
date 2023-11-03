package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.state.StateManager;

public interface BlockStateHolder {

    StateManager<Block, BlockState> getStateManager();

    BlockState getDefaultState();

    void appendProperties(StateManager.Builder<Block, BlockState> builder);

    void setDefaultState(BlockState state);
}
