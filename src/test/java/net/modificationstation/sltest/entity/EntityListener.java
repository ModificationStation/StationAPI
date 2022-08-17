package net.modificationstation.sltest.entity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;
import net.modificationstation.stationapi.api.registry.Registry;

import static net.modificationstation.sltest.SLTest.MODID;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

public class EntityListener {

    @EventListener
    public void registerEntities(EntityRegister event) {
        event.register(PoorGuy.class, "GPoor");
    }

//    @EventListener
//    public void registerEntityHandlers(EntityHandlerRegistryEvent event) {
//        event.registry.register(Identifier.of(SLTest.MODID, "gpoor"), PoorGuy::new);
//    }

    @EventListener
    public void registerMobHandlers(MobHandlerRegistryEvent event) {
        Registry.register(event.registry, of(MODID, "gpoor"), PoorGuy::new);
    }
}
