package net.modificationstation.stationapi.api.client.model.block;

import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;

@Deprecated
public interface BlockWithWorldRenderer {

    @Deprecated
    boolean renderWorld(BlockRenderer tileRenderer, BlockView tileView, int x, int y, int z);
}
