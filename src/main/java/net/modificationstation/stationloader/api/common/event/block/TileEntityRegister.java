package net.modificationstation.stationloader.api.common.event.block;

import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.Map;

public interface TileEntityRegister {

    SimpleEvent<TileEntityRegister> EVENT = new SimpleEvent<>(TileEntityRegister.class, listeners ->
            tileEntityList -> {
                for (TileEntityRegister event : listeners)
                    event.registerTileEntities(tileEntityList);
            });

    void registerTileEntities(Map<Class<?>, String> tileEntityList);
}
