package net.modificationstation.stationloader.api.client.event.gui;

import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationloader.api.common.registry.ModID;

public interface GuiHandlerRegister {

    ModEvent<GuiHandlerRegister> EVENT = new ModEvent<>(GuiHandlerRegister.class, listeners ->
            (guiHandlers, modID) -> {
        for (GuiHandlerRegister listener : listeners)
            listener.registerGUIs(guiHandlers, GuiHandlerRegister.EVENT.getListenerModID(listener));
    });

    void registerGUIs(GuiHandlerRegistry guiHandlers, ModID modID);
}
