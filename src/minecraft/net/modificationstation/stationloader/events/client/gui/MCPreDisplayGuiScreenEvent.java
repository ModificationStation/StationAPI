package net.modificationstation.stationloader.events.client.gui;

import net.minecraft.src.GuiScreen;
import net.modificationstation.stationloader.events.common.MCEvent;

public class MCPreDisplayGuiScreenEvent extends MCEvent{
    public MCPreDisplayGuiScreenEvent(GuiScreen guiscreen) {
        eventData = guiscreen;
    }
    public GuiScreen getGuiScreen() {
        return eventData;
    }
    public void setGuiScreen(GuiScreen guiscreen) {
        eventData = guiscreen;
    }
    private GuiScreen eventData;
}
