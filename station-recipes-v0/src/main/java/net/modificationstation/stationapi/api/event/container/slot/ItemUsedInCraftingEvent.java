package net.modificationstation.stationapi.api.event.container.slot;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@SuperBuilder
public class ItemUsedInCraftingEvent extends Event {
    public final PlayerEntity player;
    public final Inventory craftingMatrix;
    public final int itemOrdinal;
    public final ItemStack itemUsed;
    public final ItemStack itemCrafted;
}
