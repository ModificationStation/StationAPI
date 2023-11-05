package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResultType;

public interface CustomReachProvider {
    double getReach(ItemStack stack, PlayerEntity player, HitResultType type, double currentReach);
}
