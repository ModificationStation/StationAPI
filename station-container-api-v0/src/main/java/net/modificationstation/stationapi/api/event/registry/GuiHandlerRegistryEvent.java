package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.client.registry.GuiHandlerRegistry;

public class GuiHandlerRegistryEvent extends RegistryEvent<GuiHandlerRegistry> {
    public GuiHandlerRegistryEvent() {
        super(GuiHandlerRegistry.INSTANCE);
    }
}
