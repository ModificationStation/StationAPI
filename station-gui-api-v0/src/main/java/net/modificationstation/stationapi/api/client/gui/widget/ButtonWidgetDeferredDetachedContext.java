package net.modificationstation.stationapi.api.client.gui.widget;

import net.minecraft.client.gui.screen.Screen;

public interface ButtonWidgetDeferredDetachedContext<T extends Screen> {

    ButtonWidgetDetachedContext init(T screen);
}
