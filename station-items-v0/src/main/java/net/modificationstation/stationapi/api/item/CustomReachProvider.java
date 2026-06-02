package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

public interface CustomReachProvider {
    double getReach(ItemStack stack, PlayerEntity player, HitResult.HitResultType type, double currentReach);
}
