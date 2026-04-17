package net.modificationstation.sltest.entity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;

import static net.modificationstation.sltest.SLTest.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public class EntityListener {

    @EventListener
    public void registerEntities(EntityRegisterEvent event) {
        event.register("sltest:test", TestEntity.class);
        event.register("GPoor", PoorGuy.class);
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
