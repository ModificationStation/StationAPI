package net.modificationstation.stationapi.api.client.render.model;

import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;

public interface BakedModelRenderer {

    boolean renderWorld(BlockState block, BakedModel model, BlockView blockView, int x, int y, int z, int textureOverride);

    void renderInventory(BakedModel model);
}
