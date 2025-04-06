package net.modificationstation.stationapi.api.client.render.block;

import net.minecraft.client.render.block.BlockRenderManager;

public interface BlockRendererHelper {

    static BlockRendererHelper of(BlockRenderManager blockRenderer) {
        return () -> blockRenderer;
    }

    BlockRenderManager blockRenderer();

    default void setBottomFaceRotation(int rotation) {
        BlockRendererUtil.setBottomFaceRotation(blockRenderer(), rotation);
    }

    default void setTopFaceRotation(int rotation) {
        BlockRendererUtil.setTopFaceRotation(blockRenderer(), rotation);
    }

    default void setEastFaceRotation(int rotation) {
        BlockRendererUtil.setEastFaceRotation(blockRenderer(), rotation);
    }

    default void setWestFaceRotation(int rotation) {
        BlockRendererUtil.setWestFaceRotation(blockRenderer(), rotation);
    }

    default void setNorthFaceRotation(int rotation) {
        BlockRendererUtil.setNorthFaceRotation(blockRenderer(), rotation);
    }

    default void setSouthFaceRotation(int rotation) {
        BlockRendererUtil.setSouthFaceRotation(blockRenderer(), rotation);
    }
}
