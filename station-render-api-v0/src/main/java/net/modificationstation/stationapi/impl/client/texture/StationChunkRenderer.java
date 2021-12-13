package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.render.block.BlockRenderer;

public class StationChunkRenderer {

    public void startMeshRender(BlockRenderer blockRenderer) {
        ((BlockRendererCustomAccessor) blockRenderer).getStationBlockRenderer().startMeshRender();
    }

    public void endMeshRender(BlockRenderer blockRenderer) {
        ((BlockRendererCustomAccessor) blockRenderer).getStationBlockRenderer().endMeshRender();
    }
}
