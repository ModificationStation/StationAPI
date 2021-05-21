package net.modificationstation.stationapi.api.client.gui;

import net.minecraft.item.ItemInstance;

public interface CustomTooltipProvider {

    String[] getTooltip(ItemInstance itemInstance, String originalTooltip);
}
