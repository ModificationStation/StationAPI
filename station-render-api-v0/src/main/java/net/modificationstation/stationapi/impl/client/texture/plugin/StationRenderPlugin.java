package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.class_66;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.entity.ItemRenderer;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.ItemRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.MeshRendererPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;

public class StationRenderPlugin extends RenderPlugin {

    @Override
    public BlockRendererPlugin createBlockRenderer(BlockRenderer blockRenderer) {
        return new StationBlockRenderer(blockRenderer);
    }

    @Override
    public MeshRendererPlugin createMeshRenderer(class_66 meshRenderer) {
        return new StationChunkRenderer(meshRenderer);
    }

    @Override
    public ItemRendererPlugin createItemRenderer(ItemRenderer itemRenderer) {
        return new StationItemRenderer(itemRenderer);
    }

    @Override
    public String toString() {
        return "StationRenderer";
    }
}
