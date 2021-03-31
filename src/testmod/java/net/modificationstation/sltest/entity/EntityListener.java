package net.modificationstation.sltest.entity;

import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.registry.Identifier;

public class EntityListener {

    @EventListener
    public void registerEntities(EntityRegister event) {
        event.register(PoorGuy.class, "GPoor", 127);
    }

    @EventListener
    public void registerEntityHandlers(RegistryEvent.EntityHandlers event) {
        event.registry.registerValue(Identifier.of(SLTest.MODID, "gpoor"), PoorGuy::new);
    }
}
