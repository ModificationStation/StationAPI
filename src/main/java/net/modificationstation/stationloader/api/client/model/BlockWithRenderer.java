package net.modificationstation.stationloader.api.client.model;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.TileRenderer;
import net.minecraft.level.TileView;

public interface BlockWithRenderer {

    void renderWorld(TileRenderer tileRenderer, TileView tileView, int x, int y, int z, int meta);

    void renderInventory(TileRenderer tileRenderer, int meta);
}
