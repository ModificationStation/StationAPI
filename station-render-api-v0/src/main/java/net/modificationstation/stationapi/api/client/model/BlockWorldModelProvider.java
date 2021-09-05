package net.modificationstation.stationapi.api.client.model;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.impl.client.model.BakedModelRenderer;

public interface BlockWorldModelProvider extends BlockWithWorldRenderer {

    /**
     * The model to render in the world.
     */
    JsonModel getCustomWorldModel(BlockView blockView, int x, int y, int z);

    @Override
    default void renderWorld(BlockRenderer blockRenderer, BlockView blockView, int x, int y, int z) {
        BakedModelRenderer.renderWorld(blockRenderer, (BlockBase) this, getCustomWorldModel(blockView, x, y, z), blockView, x, y, z);
    }
}
