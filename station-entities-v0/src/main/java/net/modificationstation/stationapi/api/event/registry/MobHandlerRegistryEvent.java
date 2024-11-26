package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.client.registry.MobHandlerRegistry;

public class MobHandlerRegistryEvent extends RegistryEvent<MobHandlerRegistry> {
    public MobHandlerRegistryEvent() {
        super(MobHandlerRegistry.INSTANCE);
    }
}
