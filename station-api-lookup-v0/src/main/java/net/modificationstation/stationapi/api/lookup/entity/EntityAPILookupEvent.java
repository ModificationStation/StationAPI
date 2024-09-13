package net.modificationstation.stationapi.api.lookup.entity;

import net.mine_diver.unsafeevents.event.Cancelable;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.lookup.ApiLookupEvent;

@Cancelable
public class EntityAPILookupEvent extends ApiLookupEvent {

    public final Entity entity;

    public EntityAPILookupEvent(Class<?> apiClass, Entity entity) {
        super(apiClass);
        this.entity = entity;
    }
}
