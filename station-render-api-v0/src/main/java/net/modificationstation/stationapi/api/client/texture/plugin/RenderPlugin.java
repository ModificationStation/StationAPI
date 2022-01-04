package net.modificationstation.stationapi.api.client.texture.plugin;

import net.minecraft.class_66;
import net.minecraft.client.render.block.BlockRenderer;
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

    @Override
    public String toString() {
        return "VanillaRenderer";
    }
}
