package net.modificationstation.stationapi.api.event.container.slot;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;

@RequiredArgsConstructor
public class ItemUsedInCraftingEvent extends Event {

    public final PlayerBase player;
    public final InventoryBase craftingMatrix;
    public final int itemOrdinal;
    public final ItemInstance itemUsed;
    public final ItemInstance itemCrafted;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
