package net.modificationstation.stationapi.api.client.event.gui.screen.container;


import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuperBuilder
public class TooltipBuildEvent extends ItemStackEvent {
    /**
     * Containers aren't guaranteed to exist when rendering a tooltip.
     */
    @Nullable
    public final HandledScreen container;
    public final PlayerInventory inventory;
    public final ArrayList<String> tooltip;

    public void add(String tooltip) {
        this.tooltip.add(tooltip);
    }
}
