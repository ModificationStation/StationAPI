package net.modificationstation.stationapi.api.block;

import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;

import java.util.List;

public interface DropListProvider {

    List<ItemInstance> getDropList(Level level, int x, int y, int z, BlockState state, int meta);
}
