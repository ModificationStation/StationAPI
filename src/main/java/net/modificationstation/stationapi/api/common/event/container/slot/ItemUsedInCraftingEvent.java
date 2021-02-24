package net.modificationstation.stationapi.api.common.event.container.slot;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class ItemUsedInCraftingEvent extends Event {

    public final PlayerBase player;
    public final InventoryBase craftingMatrix;
    public final int itemOrdinal;
    public final ItemInstance itemUsed;
    public final ItemInstance itemCrafted;
}
