package net.modificationstation.stationapi.api.block;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface DropListProvider {

    List<ItemStack> getDropList(World level, int x, int y, int z, BlockState state, int meta);
}
