package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.client.registry.GuiHandlerRegistry;

public class GuiHandlerRegistryEvent extends RegistryEvent.EntryTypeBound<GuiHandler, GuiHandlerRegistry> {
    public GuiHandlerRegistryEvent() {
        super(GuiHandlerRegistry.INSTANCE);
    }
}
