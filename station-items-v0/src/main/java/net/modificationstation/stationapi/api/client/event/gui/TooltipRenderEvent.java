package net.modificationstation.stationapi.api.client.event.gui;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;

@SuperBuilder
public class TooltipRenderEvent extends ItemStackEvent {

    @Getter
    private final boolean cancelable = true;

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

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
