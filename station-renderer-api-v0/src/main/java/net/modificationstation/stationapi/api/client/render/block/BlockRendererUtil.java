package net.modificationstation.stationapi.api.client.render.block;

import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;

public final class BlockRendererUtil {

    public static void setBottomFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setBottomFaceRotation(rotation);
    }

    public static void setTopFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setTopFaceRotation(rotation);
    }

    public static void setEastFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setEastFaceRotation(rotation);
    }

    public static void setWestFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setWestFaceRotation(rotation);
    }

    public static void setNorthFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setNorthFaceRotation(rotation);
    }

    public static void setSouthFaceRotation(BlockRenderManager blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setSouthFaceRotation(rotation);
    }
}
