package net.modificationstation.stationapi.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.Context;
import org.jetbrains.annotations.Nullable;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

/**
 * Context for block-related evaluation.
 * Contains only the intrinsic state of a block in the world.
 */
@FunctionalInterface
public interface BlockContext extends Context {
    BlockContext EMPTY = id -> null;

    /**
     * The context key used to evaluate world-related conditions in a block context.
     */
    Context.Key<BlockView> BLOCK_VIEW = new Context.Key<>(NAMESPACE.id("block_view"));

    /**
     * The context key used to evaluate position-related conditions in a block context.
     */
    Context.Key<BlockPos> BLOCK_POS = new Context.Key<>(NAMESPACE.id("block_pos"));

    /**
     * The context key used to evaluate block metadata conditions.
     */
    Context.Key<Integer> BLOCK_METADATA = new Context.Key<>(NAMESPACE.id("block_metadata"));

    /**
     * {@return whether this context has block metadata}
     */
    default boolean hasBlockMeta() {
        return contains(BLOCK_METADATA) || (blockView() != null && blockPos() != null);
    }

    /**
     * {@return the block view the block interaction is occurring in}
     */
    default @Nullable BlockView blockView() { return get(BLOCK_VIEW); }

    /**
     * {@return the position of the block interaction}
     */
    default @Nullable BlockPos blockPos() { return get(BLOCK_POS); }

    /**
     * {@return the block metadata}
     */
    default int blockMeta() {
        return getIntRaw(BLOCK_METADATA.id(), 0);
    }

    interface DataProvider extends BlockContext {
        @Override
        @Nullable BlockView blockView();

        @Override
        @Nullable BlockPos blockPos();

        @Override
        default int blockMeta() {
            BlockView view = blockView();
            BlockPos pos = blockPos();
            return view != null && pos != null ? view.getBlockMeta(pos.x, pos.y, pos.z) : 0;
        }

        @Override
        default Object getRaw(Identifier id) {
            if (BLOCK_VIEW.id() == id) return blockView();
            if (BLOCK_POS.id() == id) return blockPos();
            if (BLOCK_METADATA.id() == id) return blockView() != null && blockPos() != null ? blockMeta() : null;
            return null;
        }

        @Override
        default int getIntRaw(Identifier id, int defaultValue) {
            if (BLOCK_METADATA.id() == id) return blockMeta();
            return BlockContext.super.getIntRaw(id, defaultValue);
        }
    }

    /**
     * Creates a new block context.
     */
    static BlockContext of(BlockView world, BlockPos pos) {
        record Impl(
                @Nullable BlockView blockView,
                @Nullable BlockPos blockPos
        ) implements DataProvider {}
        return new Impl(world, pos);
    }

    /**
     * Creates a new block context.
     */
    static BlockContext of(BlockView world, int x, int y, int z) {
        return of(world, new BlockPos(x, y, z));
    }

    /**
     * Projects a generic context into a block context view.
     * 
     * @param context the context to project
     * @return the block context view
     */
    static BlockContext of(Context context) {
        if (context instanceof BlockContext b) return b;
        interface BlockContextDelegate extends BlockContext, Delegate {}
        return (BlockContextDelegate) () -> context;
    }
}
