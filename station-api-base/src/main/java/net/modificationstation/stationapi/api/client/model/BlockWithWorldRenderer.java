package net.modificationstation.stationapi.api.client.model;

import net.minecraft.client.render.TileRenderer;
import net.minecraft.level.TileView;

public interface BlockWithWorldRenderer {

    void renderWorld(TileRenderer tileRenderer, TileView tileView, int x, int y, int z, int meta);
}
