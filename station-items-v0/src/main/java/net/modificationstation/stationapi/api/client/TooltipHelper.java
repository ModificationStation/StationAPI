package net.modificationstation.stationapi.api.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;

import java.util.*;

public class TooltipHelper {

    /**
     * Public helper that returns the whole
     */
    public static ArrayList<String> getTooltipForItemStack(String originalTooltip, ItemStack itemStack, PlayerInventory playerInventory, HandledScreen container) {
        ArrayList<String> newTooltip;

        if (itemStack.getItem() instanceof CustomTooltipProvider provider) {
            newTooltip = new ArrayList<>(Arrays.asList(provider.getTooltip(itemStack, originalTooltip)));
        }
        else {
            newTooltip = new ArrayList<>(){{add(originalTooltip);}};
        }

        StationAPI.EVENT_BUS.post(TooltipBuildEvent.builder()
                .tooltip(newTooltip)
                .inventory(playerInventory)
                .container(container)
                .itemStack(itemStack)
                .build()
        );

        return newTooltip;
    }
}
