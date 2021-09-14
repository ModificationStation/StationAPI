package net.modificationstation.stationapi.api.client.event.gui;

import lombok.Getter;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.item.ItemInstanceEvent;

public class TooltipRenderEvent extends ItemInstanceEvent {

    @Getter
    private final boolean cancellable = true;

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

    public TooltipRenderEvent(ItemInstance itemInstance, ContainerBase container, TextRenderer textManager, PlayerInventory inventory, int containerX, int containerY, int mouseX, int mouseY, float delta, String originalTooltip) {
        super(itemInstance);
        this.container = container;
        this.textManager = textManager;
        this.inventory = inventory;
        this.containerX = containerX;
        this.containerY = containerY;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.delta = delta;
        this.originalTooltip = originalTooltip;
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
