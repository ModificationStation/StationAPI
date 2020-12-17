package net.modificationstation.stationloader.api.client.event.gui;

import lombok.Getter;
import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationloader.api.common.registry.ModID;

public interface GuiHandlerRegister {

    ModEvent<GuiHandlerRegister> EVENT = new ModEvent<>(GuiHandlerRegister.class,
            listeners ->
                    (guiHandlers, modID) -> {
                        for (GuiHandlerRegister listener : listeners)
                            listener.registerGuiHandlers(guiHandlers, GuiHandlerRegister.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (guiHandlers, modID) -> {
                        GuiHandlerRegister.EVENT.setCurrentListener(listener);
                        listener.registerGuiHandlers(guiHandlers, modID);
                        GuiHandlerRegister.EVENT.setCurrentListener(null);
                    },
            guiHandlerRegister ->
                    guiHandlerRegister.register((guiHandlers, modID) -> ModEvent.post(new Data(guiHandlers)), null)
    );

    void registerGuiHandlers(GuiHandlerRegistry guiHandlers, ModID modID);

    final class Data extends ModEvent.Data<GuiHandlerRegister> {

        @Getter
        private final GuiHandlerRegistry registry;

        private Data(GuiHandlerRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
