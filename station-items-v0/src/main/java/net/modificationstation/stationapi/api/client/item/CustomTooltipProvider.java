package net.modificationstation.stationapi.api.client.item;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Can be implemented on Items, BlockItems and Blocks.
 * BlockItems take precedence over Blocks if both have this implemented.
 */
public interface CustomTooltipProvider {

    /**
     * @return An array of Strings, each new array entry is a new line. Supports formatting codes.
     */
    @NotNull String[] getTooltip(ItemStack stack, String originalTooltip);
}
