package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.TileRenderer;

public class StationChunkRenderer {

    public void renderAtlases(TileRenderer tileRenderer) {
        ((StationBlockRendererProvider) tileRenderer).getStationBlockRenderer().renderActiveAtlases();
    }
}
