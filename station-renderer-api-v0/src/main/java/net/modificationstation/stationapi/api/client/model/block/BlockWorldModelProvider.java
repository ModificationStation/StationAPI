package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;

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
        return BAKED_MODEL_RENDERER.get().renderWorld(((BlockStateView) blockView).getBlockState(x, y, z), getCustomWorldModel(blockView, x, y, z).getBaked(), blockView, x, y, z, ((BlockRendererAccessor) blockRenderer).getTextureOverride());
    }
}
