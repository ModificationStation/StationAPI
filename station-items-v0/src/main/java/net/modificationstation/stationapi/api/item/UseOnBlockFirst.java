package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface UseOnBlockFirst {
    boolean onUseOnBlockFirst(ItemStack stack, PlayerEntity player, World world, int x, int y, int z, int sideClicked);
}
