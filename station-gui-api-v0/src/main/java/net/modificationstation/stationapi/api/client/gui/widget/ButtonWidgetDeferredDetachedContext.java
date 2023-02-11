package net.modificationstation.stationapi.api.client.gui.widget;

import net.minecraft.client.gui.screen.ScreenBase;

public interface ButtonWidgetDeferredDetachedContext<T extends ScreenBase> {

    ButtonWidgetDetachedContext init(T screen);
}
