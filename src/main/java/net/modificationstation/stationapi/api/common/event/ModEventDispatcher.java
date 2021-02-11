package net.modificationstation.stationapi.api.common.event;

import lombok.Getter;
import net.modificationstation.stationapi.api.common.registry.ModID;

import java.util.HashMap;
import java.util.Map;

public class ModEventDispatcher {

    private final Map<ModID, EventBus<ModEvent>> eventBuses = new HashMap<>();

    @Getter
    private ModID currentListenerModID;

    public EventBus<ModEvent> getEventBus(ModID modID) {
        return eventBuses.computeIfAbsent(modID, modID1 -> new EventBus<>(ModEvent.class));
    }

    public void post(ModEvent modEvent) {
        eventBuses.forEach((modID, modEventEventBus) -> {
            currentListenerModID = modEvent.modID = modID;
            modEventEventBus.post(modEvent);
        });
        currentListenerModID = modEvent.modID = null;
    }
}
