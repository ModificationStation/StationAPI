package net.modificationstation.stationapi.api.event.container.slot;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * This is fired when an item is taken from the result slot.
 */
@SuperBuilder
public class ItemCraftedEvent extends Event {
    public final PlayerEntity player;
    public final Inventory craftingMatrix;
    public final ItemStack itemCrafted;
}
