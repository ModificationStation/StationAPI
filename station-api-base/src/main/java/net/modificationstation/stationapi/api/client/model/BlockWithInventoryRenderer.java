package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.TileRenderer;

public interface BlockWithInventoryRenderer {

    void renderInventory(TileRenderer tileRenderer, int meta);
}
