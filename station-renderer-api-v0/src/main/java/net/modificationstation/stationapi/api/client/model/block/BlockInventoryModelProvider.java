package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.model.Model;

import static net.modificationstation.stationapi.api.client.model.block.RendererHolder.BAKED_MODEL_RENDERER;

@Deprecated
public interface BlockInventoryModelProvider extends BlockWithInventoryRenderer {

    /**
     * Model to render inside the inventory.
     */
    @Deprecated
    Model getInventoryModel(int meta);

    @Override
    @Deprecated
    default void renderInventory(BlockRenderer blockRenderer, int meta) {
        BAKED_MODEL_RENDERER.get().renderInventory(getInventoryModel(meta).getBaked());
    }
}
