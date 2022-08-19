package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.util.Util;

import java.util.List;

public interface StationFlatteningBlockBase extends
        BlockStateHolder,
        DropWithBlockState,
        DropListProvider,
        AfterBreakWithBlockState,
        HardnessWithBlockState
{

    @Override
    default StateManager<BlockBase, BlockState> getStateManager() {
        return Util.assertImpl();
    }

    @Override
    default BlockState getDefaultState() {
        return Util.assertImpl();
    }

    @Override
    default void appendProperties(StateManager.Builder<BlockBase, BlockState> builder) {
        Util.assertImpl();
    }

    @Override
    default void setDefaultState(BlockState state) {
        Util.assertImpl();
    }

    @Override
    default void afterBreak(Level level, PlayerBase player, int x, int y, int z, BlockState state, int meta) {
        Util.assertImpl();
    }

    @Override
    default List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
        return Util.assertImpl();
    }

    @Override
    default void dropWithChance(Level level, int x, int y, int z, BlockState state, int meta, float chance) {
        Util.assertImpl();
    }

    @Override
    default float getHardness(BlockState state, BlockView blockView, TilePos pos) {
        return Util.assertImpl();
    }

    @Override
    default float calcBlockBreakingDelta(BlockState state, PlayerBase player, BlockView world, TilePos pos) {
        return Util.assertImpl();
    }
}
