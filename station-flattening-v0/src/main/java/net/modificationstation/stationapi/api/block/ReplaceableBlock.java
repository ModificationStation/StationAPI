package net.modificationstation.stationapi.api.block;

import net.modificationstation.stationapi.api.item.ItemPlacementContext;

public interface ReplaceableBlock {

    boolean canReplace(BlockState state, ItemPlacementContext context);
}
