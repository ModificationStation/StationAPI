package net.modificationstation.stationapi.api.client.registry;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

public final class GuiHandlerRegistry extends SimpleRegistry<GuiHandler> {
    public static final RegistryKey<GuiHandlerRegistry> KEY = RegistryKey.ofRegistry(Identifier.of(StationAPI.NAMESPACE, "gui_handlers"));
    public static final GuiHandlerRegistry INSTANCE = Registries.create(KEY, new GuiHandlerRegistry());

    private GuiHandlerRegistry() {
        super(KEY, false);
    }
}
