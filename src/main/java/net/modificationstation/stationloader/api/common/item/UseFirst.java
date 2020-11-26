package net.modificationstation.stationloader.api.common.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;

public interface UseFirst {
    public boolean onItemUseFirst(ItemInstance itemInstance, PlayerBase playerBase, Level level, int x, int y, int z, int sideClicked);
}
