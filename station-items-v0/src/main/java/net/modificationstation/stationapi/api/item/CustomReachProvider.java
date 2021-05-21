package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.hit.HitType;

public interface CustomReachProvider {

    double getReach(ItemInstance itemInstance, PlayerBase player, HitType type, double currentReach);
}
