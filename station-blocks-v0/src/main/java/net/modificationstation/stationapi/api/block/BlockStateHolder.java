package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.state.StateManager;

public interface BlockStateHolder {

    StateManager<BlockBase, BlockState> getStateManager();

    BlockState getDefaultState();

    void appendProperties(StateManager.Builder<BlockBase, BlockState> builder);

    void setDefaultState(BlockState state);
}
