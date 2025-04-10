package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.util.TriState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.jetbrains.annotations.Nullable;

/**
 * Holds, manages, and provides access to the block/world related state
 * needed to buffer quads.
 */
public class BlockRenderInfo {
    private final BlockColors blockColorMap = StationRenderAPI.getBlockColors();
    private final MutableBlockPos searchPos = new MutableBlockPos();

    public BlockView blockView;
    public BlockPos blockPos;
    public BlockState blockState;

    private boolean useAo;
    private boolean defaultAo;
    private int defaultLayer;

    private boolean enableCulling;
    private int cullCompletionFlags;
    private int cullResultFlags;

    public void prepareForWorld(BlockView blockView, boolean enableCulling) {
        this.blockView = blockView;
        this.enableCulling = enableCulling;
    }

    public void prepareForBlock(BlockPos blockPos, BlockState blockState) {
        this.blockPos = blockPos;
        this.blockState = blockState;

        useAo = Minecraft.method_2148();
        defaultAo = useAo && blockState.getLuminance() == 0;

        defaultLayer = blockState.getBlock().getRenderLayer();

        cullCompletionFlags = 0;
        cullResultFlags = 0;
    }

    public void release() {
        blockView = null;
        blockPos = null;
        blockState = null;
    }

    public int blockColor(int tintIndex) {
        return 0xFF000000 | blockColorMap.getColor(blockState, blockView, blockPos, tintIndex);
    }

    public boolean effectiveAo(TriState aoMode) {
        return useAo && aoMode.orElse(defaultAo);
    }

//    public RenderLayer effectiveRenderLayer(BlendMode blendMode) {
//        return blendMode == BlendMode.DEFAULT ? defaultLayer : blendMode.blockRenderLayer;
//    }

    public int effectiveRenderLayer(int layer) {
        return layer == 1 ? defaultLayer : layer;
    }

    public boolean shouldDrawSide(@Nullable Direction side) {
        if (side == null || !enableCulling) {
            return true;
        }

        final int mask = 1 << side.getId();

        if ((cullCompletionFlags & mask) == 0) {
            cullCompletionFlags |= mask;

            if (blockState.getBlock().isSideVisible(blockView, searchPos.getX(), searchPos.getY(), searchPos.getZ(), side.ordinal())) {
                cullResultFlags |= mask;
                return true;
            } else {
                return false;
            }
        } else {
            return (cullResultFlags & mask) != 0;
        }
    }

    public boolean shouldCullSide(@Nullable Direction side) {
        return !shouldDrawSide(side);
    }
}
