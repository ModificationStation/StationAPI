package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface StationFlatteningBlock extends
        RemappableRawIdHolder,
        ItemConvertible,
        BlockStateHolder,
        DropWithBlockState,
        DropListProvider,
        AfterBreakWithBlockState,
        HardnessWithBlockState,
        ReplaceableBlock
{

    @Override
    @ApiStatus.Internal
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    default RegistryEntry.Reference<BlockBase> getRegistryEntry() {
        return Util.assertImpl();
    }

    @Override
    default ItemBase asItem() {
        return Util.assertImpl();
    }

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

    default BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState();
    }

    @Override
    default boolean canReplace(BlockState state, ItemPlacementContext context) {
        return Util.assertImpl();
    }

    default void onBlockPlaced(Level world, int x, int y, int z, BlockState replacedState) {
        Util.assertImpl();
    }
}
