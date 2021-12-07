package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.client.render.block.BlockRendererUtil;

public interface BlockWorldModelProvider extends BlockWithWorldRenderer {

    /**
     * The model to render in the world.
     */
    Model getCustomWorldModel(BlockView blockView, int x, int y, int z);

    @Override
    default boolean renderWorld(BlockRenderer blockRenderer, BlockView blockView, int x, int y, int z) {
        return BlockRendererUtil.getBakedModelRenderer(blockRenderer).renderWorld((BlockBase) this, getCustomWorldModel(blockView, x, y, z).getBaked(), blockView, x, y, z);
    }
}
