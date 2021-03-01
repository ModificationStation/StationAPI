package net.modificationstation.stationapi.api.client.gui;

import net.minecraft.item.ItemInstance;

import java.util.*;

public interface HasCustomTooltip {

    List<String> getToolTip(String originalTooltip, ItemInstance itemInstance);
}
