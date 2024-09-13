package net.modificationstation.stationapi.api.lookup.entity;

import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.lookup.ApiLookup;
import net.modificationstation.stationapi.api.util.API;

import java.util.Optional;
import java.util.function.Function;

public final class EntityAPILookup {

    @API
    public <T> Optional<T> find(Class<T> api, Entity entity) {
        return ApiLookup.fromEvent(api, new EntityAPILookupEvent(api, entity));
    }

    @API
    public <T> Function<Entity, Optional<T>> finder(Class<T> api) {
        return entity -> this.find(api, entity);
    }

}
