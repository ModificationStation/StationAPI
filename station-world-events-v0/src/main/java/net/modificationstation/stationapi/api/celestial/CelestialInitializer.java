package net.modificationstation.stationapi.api.celestial;

import java.util.LinkedList;
import java.util.List;

/**
 * Automatically handles initialization of celestial events.
 * Ensures that all celestial events are loaded correctly.
 */
public class CelestialInitializer {
    private static final List<CelestialEvent> ALL_EVENTS = new LinkedList<>();

    /**
     * Called automatically by every celestial event.
     * @param celestialEvent The event to be added.
     */
    public static void addEvent(CelestialEvent celestialEvent) {
        ALL_EVENTS.add(celestialEvent);
    }

    /**
     * Prevents duplicates when worlds are switched.
     */
    public static void clearList() {
        ALL_EVENTS.clear();
    }

    /**
     * Initializes all events when the world is loaded, ensures correct loading of active events.
     */
    public static void initializeEvents() {
        for (CelestialEvent celestialEvent : ALL_EVENTS) {
            if (celestialEvent == null) continue;
            celestialEvent.markForInitialization();
        }
    }
}
