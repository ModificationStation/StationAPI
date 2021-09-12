package net.modificationstation.stationapi.api.client.event.gui;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.item.ItemInstance;

@RequiredArgsConstructor
public class CustomTooltipProviderEvent extends Event {

    public final ItemInstance itemInstance;
    public final ContainerBase container;
    public final TextRenderer textManager;
    public final int containerX;
    public final int containerY;
    public final int mouseX;
    public final int mouseY;

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
