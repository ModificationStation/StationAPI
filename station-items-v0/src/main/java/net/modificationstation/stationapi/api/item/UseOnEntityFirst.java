package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface UseOnEntityFirst {
    boolean onUseOnEntityFirst(ItemStack itemInstance, PlayerEntity playerBase, World level, Entity entityBase);
}
