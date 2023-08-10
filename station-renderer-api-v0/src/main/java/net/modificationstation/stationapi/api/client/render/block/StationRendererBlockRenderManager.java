package net.modificationstation.stationapi.api.client.render.block;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationRendererBlockRenderManager {
    default void renderAllSides(BlockState state, int x, int y, int z) {
        Util.assertImpl();
    }
}
