package net.modificationstation.stationloader.api.common.item;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;

public interface UseOnEntityFirst {
    boolean onUseOnEntityFirst(ItemInstance itemInstance, PlayerBase playerBase, Level level, EntityBase entityBase);
}
