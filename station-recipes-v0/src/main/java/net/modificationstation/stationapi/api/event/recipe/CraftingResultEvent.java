package net.modificationstation.stationapi.api.event.recipe;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * This is fired when a crafting recipe result is created.
 * You can set the itemCrafted field to whatever item stack should be used instead.
 */
@SuperBuilder
public class CraftingResultEvent extends Event {
    public final Inventory grid;
    @Nullable
    public ItemStack itemCrafted;
}
