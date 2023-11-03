package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.collection.IdList;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.ToIntFunction;

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

    IdList<BlockState> STATE_IDS = new IdList();

    @Override
    @ApiStatus.Internal
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    default RegistryEntry.Reference<Block> getRegistryEntry() {
        return Util.assertImpl();
    }

    @Override
    default Item asItem() {
        return Util.assertImpl();
    }

    @Override
    default StateManager<Block, BlockState> getStateManager() {
        return Util.assertImpl();
    }

    @Override
    default BlockState getDefaultState() {
        return Util.assertImpl();
    }

    @Override
    default void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        Util.assertImpl();
    }

    @Override
    default void setDefaultState(BlockState state) {
        Util.assertImpl();
    }

    @Override
    default void afterBreak(World level, PlayerEntity player, int x, int y, int z, BlockState state, int meta) {
        Util.assertImpl();
    }

    @Override
    default List<ItemStack> getDropList(World level, int x, int y, int z, BlockState state, int meta) {
        return Util.assertImpl();
    }

    @Override
    default void dropWithChance(World level, int x, int y, int z, BlockState state, int meta, float chance) {
        Util.assertImpl();
    }

    @Override
    default float getHardness(BlockState state, BlockView blockView, BlockPos pos) {
        return Util.assertImpl();
    }

    @Override
    default float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        return Util.assertImpl();
    }

    default BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState();
    }

    @Override
    default boolean canReplace(BlockState state, ItemPlacementContext context) {
        return Util.assertImpl();
    }

    default void onBlockPlaced(World world, int x, int y, int z, BlockState replacedState) {
        Util.assertImpl();
    }

    default Block setLuminance(ToIntFunction<BlockState> provider) {
        return Util.assertImpl();
    }
}
