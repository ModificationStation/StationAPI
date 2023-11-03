package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderManager;

@Deprecated
public interface BlockWithInventoryRenderer {

    @Deprecated
    void renderInventory(BlockRenderManager tileRenderer, int meta);
}
