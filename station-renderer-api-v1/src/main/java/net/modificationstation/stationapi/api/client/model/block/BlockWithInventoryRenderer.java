package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;

@Deprecated
public interface BlockWithInventoryRenderer {

    @Deprecated
    void renderInventory(VertexConsumer consumer, BlockRenderManager tileRenderer, int meta);
}
