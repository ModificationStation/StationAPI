package net.modificationstation.stationapi.api.client.event.gui;

import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;

public class GuiHandlerRegister extends RegistryEvent<GuiHandlerRegistry> {

    public GuiHandlerRegister() {
        super(GuiHandlerRegistry.INSTANCE);
    }
}
