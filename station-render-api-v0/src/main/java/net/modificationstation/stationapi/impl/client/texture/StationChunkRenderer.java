package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.block.BlockRenderer;

public class StationChunkRenderer {

    public void renderAtlases(BlockRenderer tileRenderer) {
        ((StationBlockRendererProvider) tileRenderer).getStationBlockRenderer().renderActiveAtlases();
    }
}
