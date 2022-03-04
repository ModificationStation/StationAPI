package net.modificationstation.stationapi.api.client.colour.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;

@Environment(EnvType.CLIENT)
public interface ItemColourProvider {

    /**
     * {@return the color of the item stack for the specified tint index,
     * or -1 if not tinted}
     */
    int getColour(ItemInstance var1, int var2);
}

