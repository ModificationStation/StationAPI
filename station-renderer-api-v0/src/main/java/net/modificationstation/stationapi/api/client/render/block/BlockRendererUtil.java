package net.modificationstation.stationapi.api.client.render.block;

import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.stationapi.mixin.render.client.BlockRenderManagerAccessor;

public final class BlockRendererUtil {

    public static void setBottomFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRenderManagerAccessor) blockRenderer).setBottomFaceRotation(rotation);
    }

    public static void setTopFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRenderManagerAccessor) blockRenderer).setTopFaceRotation(rotation);
    }

    public static void setEastFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRenderManagerAccessor) blockRenderer).setEastFaceRotation(rotation);
    }

    public static void setWestFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRenderManagerAccessor) blockRenderer).setWestFaceRotation(rotation);
    }

    public static void setNorthFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRenderManagerAccessor) blockRenderer).setNorthFaceRotation(rotation);
    }

    public static void setSouthFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRenderManagerAccessor) blockRenderer).setSouthFaceRotation(rotation);
    }
}
