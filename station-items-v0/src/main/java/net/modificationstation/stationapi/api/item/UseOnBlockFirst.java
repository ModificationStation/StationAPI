package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;

public interface UseOnBlockFirst {
    boolean onUseOnBlockFirst(ItemInstance itemInstance, PlayerBase playerBase, Level level, int x, int y, int z, int sideClicked);
}
