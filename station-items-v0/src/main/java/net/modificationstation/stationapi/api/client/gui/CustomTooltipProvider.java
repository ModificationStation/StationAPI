package net.modificationstation.stationapi.api.client.gui;

import net.minecraft.item.ItemStack;

public interface CustomTooltipProvider {

    String[] getTooltip(ItemStack itemInstance, String originalTooltip);
}
