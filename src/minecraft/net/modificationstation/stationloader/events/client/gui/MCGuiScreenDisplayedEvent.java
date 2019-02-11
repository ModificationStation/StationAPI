package net.modificationstation.stationloader.events.client.gui;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import net.modificationstation.stationloader.events.common.MCEvent;

public class MCGuiScreenDisplayedEvent extends MCEvent{
    public MCGuiScreenDisplayedEvent(GuiScreen guiscreen, ScaledResolution scaledresolution, int i, int j) {
        eventData = new Object[] {guiscreen, scaledresolution, i, j};
    }
    public GuiScreen getGuiScreen() {
        return (GuiScreen) eventData[0];
    }
    public ScaledResolution getScaledResolution() {
        return (ScaledResolution) eventData[1];
    }
    public int getWidth() {
        return (Integer) eventData[2];
    }
    public int getHeight() {
        return (Integer) eventData[3];
    }
    private Object eventData[];
}
