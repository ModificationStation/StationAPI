package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;

@Deprecated
public interface BlockWithWorldRenderer {

    @Deprecated
    boolean renderWorld(BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z);
}
