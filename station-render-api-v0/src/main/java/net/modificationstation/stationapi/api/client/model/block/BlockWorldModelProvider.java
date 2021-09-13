package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.impl.client.model.BakedModelRenderer;

public interface BlockWorldModelProvider extends BlockWithWorldRenderer {

    /**
     * The model to render in the world.
     */
    Model getCustomWorldModel(BlockView blockView, int x, int y, int z);

    @Override
    default void renderWorld(BlockRenderer blockRenderer, BlockView blockView, int x, int y, int z) {
        BakedModelRenderer.renderWorld(blockRenderer, (BlockBase) this, getCustomWorldModel(blockView, x, y, z).getBaked(), blockView, x, y, z);
    }
}
