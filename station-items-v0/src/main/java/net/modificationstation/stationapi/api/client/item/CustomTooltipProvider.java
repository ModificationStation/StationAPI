package net.modificationstation.stationapi.api.client.item;

import net.minecraft.item.ItemStack;

public interface CustomTooltipProvider {
    String[] getTooltip(ItemStack stack, String originalTooltip);
}
