package net.modificationstation.stationapi.api.client.render.block;

import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;

public final class BlockRendererUtil {

    public static void setBottomFaceRotation(BlockRenderer blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setBottomFaceRotation(rotation);
    }

    public static void setTopFaceRotation(BlockRenderer blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setTopFaceRotation(rotation);
    }

    public static void setEastFaceRotation(BlockRenderer blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setEastFaceRotation(rotation);
    }

    public static void setWestFaceRotation(BlockRenderer blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setWestFaceRotation(rotation);
    }

    public static void setNorthFaceRotation(BlockRenderer blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setNorthFaceRotation(rotation);
    }

    public static void setSouthFaceRotation(BlockRenderer blockRenderer, int rotation) {
        ((BlockRendererAccessor) blockRenderer).setSouthFaceRotation(rotation);
    }
}
