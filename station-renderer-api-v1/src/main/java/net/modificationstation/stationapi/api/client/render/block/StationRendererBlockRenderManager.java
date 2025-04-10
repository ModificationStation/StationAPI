package net.modificationstation.stationapi.api.client.render.block;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.util.Util;

public interface StationRendererBlockRenderManager {
    default void renderAllSides(VertexConsumer consumer, BlockState state, int x, int y, int z) {
        Util.assertImpl();
    }

    default void setVertexConsumer(VertexConsumer vertexConsumer) {
        Util.assertImpl();
    }

    default VertexConsumer getVertexConsumer() {
        return Util.assertImpl();
    }
}
