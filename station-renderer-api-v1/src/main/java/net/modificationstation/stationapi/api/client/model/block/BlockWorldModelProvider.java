package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.Random;

import static net.modificationstation.stationapi.api.client.model.block.RendererHolder.BAKED_MODEL_RENDERER;

@Deprecated
public interface BlockWorldModelProvider extends BlockWithWorldRenderer {

    /**
     * The model to render in the world.
     */
    @Deprecated
    Model getCustomWorldModel(BlockView blockView, int x, int y, int z);

    @Override
    @Deprecated
    default boolean renderWorld(VertexConsumer consumer, BlockRenderManager blockRenderer, BlockView blockView, int x, int y, int z) {
        BlockState state = ((BlockStateView) blockView).getBlockState(x, y, z);
        BlockPos pos = new BlockPos(x, y, z);
        return BAKED_MODEL_RENDERER.get().render(consumer, blockView, getCustomWorldModel(blockView, x, y, z).getBaked(), state, pos, true, new Random(), state.getRenderingSeed(pos));
    }
}
