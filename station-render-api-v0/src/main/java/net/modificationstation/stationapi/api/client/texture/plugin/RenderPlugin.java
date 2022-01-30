package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.class_556;
import net.minecraft.class_66;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.texture.plugin.ProvideRenderPluginEvent;

public class RenderPlugin {

    public static final RenderPlugin PLUGIN = StationAPI.EVENT_BUS.post(new ProvideRenderPluginEvent()).pluginProvider.get();

    public BlockRendererPlugin createBlockRenderer(BlockRenderer blockRenderer) {
        return new BlockRendererPlugin(blockRenderer);
    }

    public MeshRendererPlugin createMeshRenderer(class_66 meshRenderer) {
        return new MeshRendererPlugin(meshRenderer);
    }

    public ItemRendererPlugin createItemRenderer(ItemRenderer itemRenderer) {
        return new ItemRendererPlugin(itemRenderer);
    }

    public OverlayRendererPlugin createOverlayRenderer(class_556 overlayRenderer) {
        return new OverlayRendererPlugin(overlayRenderer);
    }

    public TextureManagerPlugin createTextureManager(TextureManager textureManager) {
        return new TextureManagerPlugin(textureManager);
    }

    public TessellatorPlugin createTessellator(Tessellator tessellator) {
        return new TessellatorPlugin(tessellator);
    }

    @Override
    public String toString() {
        return "Vanilla";
    }
}
