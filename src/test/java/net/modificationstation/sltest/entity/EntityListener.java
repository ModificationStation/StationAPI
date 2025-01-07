package net.modificationstation.sltest.entity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;

import static net.modificationstation.sltest.SLTest.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public class EntityListener {

    @EventListener
    public void registerEntities(EntityRegister event) {
        event.register(TestEntity.class, "sltest:test");
        event.register(PoorGuy.class, "GPoor");
    }

    @EventListener
    public void registerEntityHandlers(EntityHandlerRegistryEvent event) {
        event.register(TestEntity.ID, TestEntity::new);
    }

    @EventListener
    public void registerMobHandlers(MobHandlerRegistryEvent event) {
        event.register(of(NAMESPACE, "gpoor"), PoorGuy::new);
    }
}
