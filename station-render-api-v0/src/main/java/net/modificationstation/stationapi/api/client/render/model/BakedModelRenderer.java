package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.block.BlockBase;
import net.minecraft.level.BlockView;

public interface BakedModelRenderer {

    boolean renderWorld(BlockBase block, BakedModel model, BlockView blockView, int x, int y, int z);

    void renderInventory(BakedModel model);
}
