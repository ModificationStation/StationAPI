package net.modificationstation.stationapi.api.client.event.gui.screen.container;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;
import org.jetbrains.annotations.Nullable;

@Cancelable
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class TooltipRenderEvent extends ItemStackEvent {
    /**
     * Containers aren't guaranteed to exist when rendering a tooltip.
     */
    @Nullable
    public final HandledScreen container;
    public final TextRenderer textManager;
    public final PlayerInventory inventory;
    /**
     * These will be removed in a3, due to the fact the container var already exists.
     */
    @Deprecated(forRemoval = true)
    public final int
            containerX,
            containerY;
    public final int
            mouseX,
            mouseY;
    public final float delta;
    public final String originalTooltip;
}
