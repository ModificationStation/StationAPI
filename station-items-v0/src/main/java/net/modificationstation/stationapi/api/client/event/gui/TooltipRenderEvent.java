package net.modificationstation.stationapi.api.client.event.gui;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;

@Cancelable
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class TooltipRenderEvent extends ItemStackEvent {
    public final ContainerBase container;
    public final TextRenderer textManager;
    public final PlayerInventory inventory;
    public final int
            containerX,
            containerY,
            mouseX,
            mouseY;
    public final float delta;
    public final String originalTooltip;
}
