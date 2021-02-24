package net.modificationstation.stationapi.api.common.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.util.hit.HitType;

public interface ICustomReach {

    double getReach(ItemInstance itemInstance, PlayerBase player, HitType type, double currentReach);
}
