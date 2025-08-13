package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;

@Deprecated
public interface BlockWithWorldRenderer {

    @Deprecated
    boolean renderWorld(VertexConsumer consumer, BlockRenderManager tileRenderer, BlockView tileView, int x, int y, int z);
}
