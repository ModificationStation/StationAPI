package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.model.Model;
import net.modificationstation.stationapi.api.client.render.block.BlockRendererUtil;

public interface BlockInventoryModelProvider extends BlockWithInventoryRenderer {

    /**
     * Model to render inside the inventory.
     */
    Model getInventoryModel(int meta);

    @Override
    default void renderInventory(BlockRenderer blockRenderer, int meta) {
        BlockRendererUtil.getBakedModelRenderer(blockRenderer).renderInventory(getInventoryModel(meta).getBaked());
    }
}
