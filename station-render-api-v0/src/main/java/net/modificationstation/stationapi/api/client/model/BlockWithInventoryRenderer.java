package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.block.BlockRenderer;

public interface BlockWithInventoryRenderer {

    void renderInventory(BlockRenderer tileRenderer, int meta);
}
