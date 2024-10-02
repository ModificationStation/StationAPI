package net.modificationstation.stationapi.api.lookup;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.lookup.block.BlockAPILookup;
import net.modificationstation.stationapi.api.lookup.entity.EntityAPILookup;
import net.modificationstation.stationapi.api.lookup.item.ItemAPILookup;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.exception.LookupException;

import java.util.Optional;

public class ApiLookup {

    @API
    public static final ItemAPILookup ITEM = new ItemAPILookup();
    @API
    public static final BlockAPILookup BLOCK = new BlockAPILookup();
    @API
    public static final EntityAPILookup ENTITY = new EntityAPILookup();

    public static <T> Optional<T> fromEvent(Class<T> api, ApiLookupEvent event) {
        if (!api.isAssignableFrom(event.apiClass)) {
            throw new LookupException(api, event.apiClass);
        }

        return StationAPI.EVENT_BUS.post(event)
            .getApi()
            .filter(api::isInstance)
            .map(api::cast);
    }

}
