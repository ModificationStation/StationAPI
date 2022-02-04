package net.modificationstation.stationapi.api.client.render.block;

import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPluginProvider;
import net.modificationstation.stationapi.impl.client.texture.plugin.StationBlockRenderer;
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

    public static BakedModelRenderer getBakedModelRenderer(BlockRenderer blockRenderer) {
        BlockRendererPlugin plugin = ((BlockRendererPluginProvider) blockRenderer).getPlugin();
        if (plugin instanceof StationBlockRenderer)
            return ((StationBlockRenderer) plugin).bakedModelRenderer;
        else
            throw new IllegalStateException("Current plugin isn't instance of StationBlockRenderer!");
    }
}
