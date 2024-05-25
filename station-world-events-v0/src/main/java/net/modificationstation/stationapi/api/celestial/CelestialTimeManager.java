package net.modificationstation.stationapi.api.celestial;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Manages the activation and deactivation of celestial events.
 * Has been injected into WorldProperties using the WorldPropertiesMixin in station-world-events, does not need to be handled by mods.
 * Events need to be added during the registering process.
 * Uses lists to schedule event updates during the correct time of day.
 * Called four times per day.
 */
public class CelestialTimeManager {
    private static final List<CelestialEvent> MORNING_START = new LinkedList<>();
    private static final List<CelestialEvent> NOON_START = new LinkedList<>();
    private static final List<CelestialEvent> EVENING_START = new LinkedList<>();
    private static final List<CelestialEvent> MIDNIGHT_START = new LinkedList<>();
    private static final List<CelestialEvent> ALL_EVENTS = new LinkedList<>();

    private static boolean morningActivation = false;
    private static boolean noonActivation = false;
    private static boolean eveningActivation = false;
    private static boolean midnightActivation = false;

    private static long lastCheckedDay = 0;

    private static final Random RANDOM = new Random();

    /**
     * Add a celestial event to the time manager.
     *
     * @param celestialEvent Event to be added.
     * @param start Time of day at which the event begins.
     * @param stop Time of day at which the event ends.
     */
    public static void addCelestialEvent(CelestialEvent celestialEvent, DayQuarter start, DayQuarter stop) {
        switch (start) {
            case MORNING -> MORNING_START.add(celestialEvent);
            case NOON -> NOON_START.add(celestialEvent);
            case EVENING -> EVENING_START.add(celestialEvent);
            case MIDNIGHT -> MIDNIGHT_START.add(celestialEvent);
        }
        ALL_EVENTS.add(celestialEvent);
        celestialEvent.setInterval(start.ordinal() * 6000, Math.abs(stop.ordinal() * 6000 - start.ordinal() * 6000));
    }

    /**
     * Attempts to start all morning events. Called once per day.
     *
     * @param time World time in ticks.
     * @param currentDay Current day in the world.
     */
    public static void startMorningEvents(long time, long currentDay) {
        if (morningActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = true;
        noonActivation = false;
        eveningActivation = false;
        midnightActivation = false;
        updateEvents(time);
        for (CelestialEvent celestialEvent : MORNING_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
    }

    /**
     * Attempts to start all noon events. Called once per day.
     *
     * @param time World time in ticks.
     * @param currentDay Current day in the world.
     */
    public static void startNoonEvents(long time, long currentDay) {
        if (noonActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = true;
        eveningActivation = false;
        midnightActivation = false;
        updateEvents(time);
        for (CelestialEvent celestialEvent : NOON_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
    }

    /**
     * Attempts to start all evening events. Called once per day.
     *
     * @param time World time in ticks.
     * @param currentDay Current day in the world.
     */
    public static void startEveningEvents(long time, long currentDay) {
        if (eveningActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = false;
        eveningActivation = true;
        midnightActivation = false;
        updateEvents(time);
        for (CelestialEvent celestialEvent : EVENING_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
    }

    /**
     * Attempts to start all midnight events. Called once per day.
     *
     * @param time World time in ticks.
     * @param currentDay Current day in the world.
     */
    public static void startMidnightEvents(long time, long currentDay) {
        if (midnightActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = false;
        eveningActivation = false;
        midnightActivation = true;
        updateEvents(time);
        for (CelestialEvent celestialEvent : MIDNIGHT_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
    }

    /**
     * Updates activity state of all events. Called four times per day.
     *
     * @param time World time in ticks.
     */
    public static void updateEvents(long time) {
        for (CelestialEvent celestialEvent : ALL_EVENTS) {
            if (celestialEvent == null) continue;
            celestialEvent.updateEvent(time);
        }
    }

    /**
     * Clears all lists. Ensures that no duplicates show up after switching worlds.
     */
    public static void clearLists() {
        MORNING_START.clear();
        NOON_START.clear();
        EVENING_START.clear();
        MIDNIGHT_START.clear();
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
