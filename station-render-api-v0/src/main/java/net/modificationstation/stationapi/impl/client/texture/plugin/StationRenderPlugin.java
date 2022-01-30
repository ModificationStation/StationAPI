package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.class_556;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.ItemRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.OverlayRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.TessellatorPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.TextureManagerPlugin;

public class StationRenderPlugin extends RenderPlugin {

    @Override
    public BlockRendererPlugin createBlockRenderer(BlockRenderer blockRenderer) {
        return new StationBlockRenderer(blockRenderer);
    }

    @Override
    public ItemRendererPlugin createItemRenderer(ItemRenderer itemRenderer) {
        return new StationItemRenderer(itemRenderer);
    }

    @Override
    public OverlayRendererPlugin createOverlayRenderer(class_556 overlayRenderer) {
        return new StationOverlayRenderer(overlayRenderer);
    }

    @Override
    public TextureManagerPlugin createTextureManager(TextureManager textureManager) {
        return new StationTextureManager(textureManager);
    }

    @Override
    public TessellatorPlugin createTessellator(Tessellator tessellator) {
        return new StationTessellator(tessellator);
    }

    @Override
    public String toString() {
        return "StationRenderer";
    }
}
