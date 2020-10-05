package net.modificationstation.stationloader.api.common.event.block;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

import java.util.HashMap;

public interface TileEntityRegister {

    Event<TileEntityRegister> EVENT = EventFactory.INSTANCE.newEvent(TileEntityRegister.class, (listeners) ->
            (tileEntityList) -> {
                for (TileEntityRegister event : listeners)
                    event.registerTileEntities(tileEntityList);
            });

    void registerTileEntities(HashMap<String, Class> tileEntityList);
}
