package net.modificationstation.stationapi.api.lookup;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.exception.LookupException;

import java.util.*;
import java.util.function.BiFunction;

public class EventBasedAPILookup<S> {

    private final Map<S, Object> cache = new HashMap<>();
    private final Set<S> misses = new HashSet<>();

    private final BiFunction<S, Class<?>, ? extends ApiLookupEvent> eventFactory;

    public EventBasedAPILookup(BiFunction<S, Class<?>, ? extends ApiLookupEvent> eventFactory) {
        this.eventFactory = eventFactory;
    }

    public <T> Optional<T> find(Class<T> api, S object) {
        ApiLookupEvent event = eventFactory.apply(object, api);

        if (misses.contains(object)) {
            return Optional.empty();
        }

        var cached = cache.get(object);
        if (api.isInstance(cached)) {
            return Optional.of((T) cached);
        }

        if (event.apiClass.isAssignableFrom(api)) {
            var result = StationAPI.EVENT_BUS.post(event).getApi().filter(api::isInstance).map(api::cast);
            if (result.isPresent()) {
                cache.put(object, result);
            } else {
                misses.add(object);
            }
            return result;
        } else {
            throw new LookupException(api, event.apiClass);
        }
    }
}
