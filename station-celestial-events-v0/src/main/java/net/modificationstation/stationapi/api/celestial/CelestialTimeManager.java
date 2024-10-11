package net.modificationstation.stationapi.api.celestial;

import net.minecraft.world.World;

import java.util.*;

/**
 * Manages the activation and deactivation of celestial events.
 * Has been injected into WorldProperties using the WorldPropertiesMixin in station-world-events, does not need to be handled by mods.
 * Events need to be added during the registering process.
 * Uses lists to schedule event updates during the correct time of day.
 * Called four times per day.
 */
public class CelestialTimeManager {
    private final List<CelestialEvent> MORNING_START = new ArrayList<>();
    private final List<CelestialEvent> NOON_START = new LinkedList<>();
    private final List<CelestialEvent> EVENING_START = new LinkedList<>();
    private final List<CelestialEvent> MIDNIGHT_START = new LinkedList<>();
    private final List<CelestialEvent> ALL_EVENTS = new LinkedList<>();

    private boolean morningActivation = false;
    private boolean noonActivation = false;
    private boolean eveningActivation = false;
    private boolean midnightActivation = false;

    private long lastCheckedDay = 0;

    private final Random RANDOM = new Random();

    private final World world;

    public CelestialTimeManager(World world) {
        this.world = world;
    }

    /**
     * Add a celestial event to the time manager.
     *
     * @param celestialEvent Event to be added.
     * @param start Time of day at which the event begins.
     * @param stop Time of day at which the event ends.
     */
    public void addCelestialEvent(CelestialEvent celestialEvent, DayQuarter start, DayQuarter stop) {
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
    public void startMorningEvents(long time, long currentDay) {
        if (morningActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = true;
        noonActivation = false;
        eveningActivation = false;
        midnightActivation = false;
        updateEvents(time);
        for (CelestialEvent celestialEvent : MORNING_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(world, time, RANDOM);
        }
    }

    /**
     * Attempts to start all noon events. Called once per day.
     *
     * @param time World time in ticks.
     * @param currentDay Current day in the world.
     */
    public void startNoonEvents(long time, long currentDay) {
        if (noonActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = true;
        eveningActivation = false;
        midnightActivation = false;
        updateEvents(time);
        for (CelestialEvent celestialEvent : NOON_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(world, time, RANDOM);
        }
    }

    /**
     * Attempts to start all evening events. Called once per day.
     *
     * @param time World time in ticks.
     * @param currentDay Current day in the world.
     */
    public void startEveningEvents(long time, long currentDay) {
        if (eveningActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = false;
        eveningActivation = true;
        midnightActivation = false;
        updateEvents(time);
        for (CelestialEvent celestialEvent : EVENING_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(world, time, RANDOM);
        }
    }

    /**
     * Attempts to start all midnight events. Called once per day.
     *
     * @param time World time in ticks.
     * @param currentDay Current day in the world.
     */
    public void startMidnightEvents(long time, long currentDay) {
        if (midnightActivation && lastCheckedDay == currentDay) return;
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = false;
        eveningActivation = false;
        midnightActivation = true;
        updateEvents(time);
        for (CelestialEvent celestialEvent : MIDNIGHT_START) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(world, time, RANDOM);
        }
    }

    /**
     * Updates activity state of all events. Called four times per day.
     *
     * @param time World time in ticks.
     */
    public void updateEvents(long time) {
        for (CelestialEvent celestialEvent : ALL_EVENTS) {
            if (celestialEvent == null) continue;
            celestialEvent.updateEvent(world, time);
        }
    }
}
