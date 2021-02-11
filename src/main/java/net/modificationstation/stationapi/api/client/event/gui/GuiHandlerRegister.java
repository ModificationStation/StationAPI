package net.modificationstation.stationapi.api.client.event.gui;

import net.modificationstation.stationapi.api.common.event.ModEventOld;
import net.modificationstation.stationapi.api.common.gui.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.common.registry.ModID;

public interface GuiHandlerRegister {

    ModEventOld<GuiHandlerRegister> EVENT = new ModEventOld<>(GuiHandlerRegister.class,
            listeners ->
                    (registry, modID) -> {
                        for (GuiHandlerRegister listener : listeners)
                            listener.registerGuiHandlers(registry, GuiHandlerRegister.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (registry, modID) -> {
                        GuiHandlerRegister.EVENT.setCurrentListener(listener);
                        listener.registerGuiHandlers(registry, modID);
                        GuiHandlerRegister.EVENT.setCurrentListener(null);
                    },
            guiHandlerRegister ->
                    guiHandlerRegister.register((registry, modID) -> ModEventOld.post(new Data(registry)), null)
    );

    void registerGuiHandlers(GuiHandlerRegistry registry, ModID modID);

    final class Data extends ModEventOld.Data<GuiHandlerRegister> {

        public final GuiHandlerRegistry registry;

        private Data(GuiHandlerRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
