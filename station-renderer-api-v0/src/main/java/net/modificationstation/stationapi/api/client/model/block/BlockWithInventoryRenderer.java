package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderer;

@Deprecated
public interface BlockWithInventoryRenderer {

    @Deprecated
    void renderInventory(BlockRenderer tileRenderer, int meta);
}
