package net.modificationstation.stationapi.api.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public interface DropListProvider {
    List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta);
}
