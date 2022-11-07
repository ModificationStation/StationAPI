package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.client.render.OverlayTexture;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.api.util.math.MatrixStack;

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
    default boolean renderWorld(BlockRenderer blockRenderer, BlockView blockView, int x, int y, int z) {
        BlockState state = ((BlockStateView) blockView).getBlockState(x, y, z);
        TilePos pos = new TilePos(x, y, z);
        return BAKED_MODEL_RENDERER.get().render(blockView, getCustomWorldModel(blockView, x, y, z).getBaked(), state, pos, new MatrixStack(), StationTessellator.get(Tessellator.INSTANCE), true, new Random(), state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
    }
}
