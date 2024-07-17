package net.modificationstation.stationapi.impl.config.screen;

import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.*;

public interface ScreenAccessor {

    List<String> getMouseTooltip(int mouseX, int mouseY, List<?> extraObjectsToCheck);

    void setSelectedButton(ButtonWidget value);
}
