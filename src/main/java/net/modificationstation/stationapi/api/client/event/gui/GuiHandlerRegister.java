package net.modificationstation.stationapi.api.client.event.gui;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;

@RequiredArgsConstructor
public class GuiHandlerRegister extends Event {

    public final GuiHandlerRegistry registry;
}
