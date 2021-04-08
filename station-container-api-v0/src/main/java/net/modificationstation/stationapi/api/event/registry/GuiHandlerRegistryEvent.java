package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;

public class GuiHandlerRegistryEvent extends RegistryEvent<GuiHandlerRegistry> {

    public GuiHandlerRegistryEvent() {
        super(GuiHandlerRegistry.INSTANCE);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
