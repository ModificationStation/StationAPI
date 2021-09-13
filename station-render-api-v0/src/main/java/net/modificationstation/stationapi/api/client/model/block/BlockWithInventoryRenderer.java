package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderer;

public interface BlockWithInventoryRenderer {

    void renderInventory(BlockRenderer tileRenderer, int meta);
}
