package net.modificationstation.stationapi.api.celestial;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CelestialTimeManager {
    private static final List<CelestialEvent> MORNING_LIST = new LinkedList<>();
    private static final List<CelestialEvent> NOON_LIST = new LinkedList<>();
    private static final List<CelestialEvent> EVENING_LIST = new LinkedList<>();
    private static final List<CelestialEvent> MIDNIGHT_LIST = new LinkedList<>();

    private static boolean morningActivation = false;
    private static boolean noonActivation = false;
    private static boolean eveningActivation = false;
    private static boolean midnightActivation = false;

    private static long lastCheckedDay = 0;

    private static final Random RANDOM = new Random();

    public static void addMorningEvent(CelestialEvent celestialEvent) {
        MORNING_LIST.add(celestialEvent);
    }

    public static void addNoonEvent(CelestialEvent celestialEvent) {
        NOON_LIST.add(celestialEvent);
    }

    public static void addEveningEvent(CelestialEvent celestialEvent) {
        EVENING_LIST.add(celestialEvent);
    }

    public static void addMidnightEvent(CelestialEvent celestialEvent) {
        MIDNIGHT_LIST.add(celestialEvent);
    }

    public static void startMorningEvents(long time, long currentDay) {
        if (morningActivation && lastCheckedDay == currentDay) return;
        stopMorningEvents();
        lastCheckedDay = currentDay;
        morningActivation = true;
        noonActivation = false;
        eveningActivation = false;
        midnightActivation = false;
        for (CelestialEvent celestialEvent : MORNING_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
        stopNoonEvents();
        stopEveningEvents();
    }

    public static void startNoonEvents(long time, long currentDay) {
        if (noonActivation && lastCheckedDay == currentDay) return;
        stopNoonEvents();
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = true;
        eveningActivation = false;
        midnightActivation = false;
        for (CelestialEvent celestialEvent : NOON_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
        stopEveningEvents();
        stopMidnightEvents();
    }

    public static void startEveningEvents(long time, long currentDay) {
        if (eveningActivation && lastCheckedDay == currentDay) return;
        stopEveningEvents();
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = false;
        eveningActivation = true;
        midnightActivation = false;
        for (CelestialEvent celestialEvent : EVENING_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
        stopMidnightEvents();
        stopMorningEvents();
    }

    public static void startMidnightEvents(long time, long currentDay) {
        if (midnightActivation && lastCheckedDay == currentDay) return;
        stopMidnightEvents();
        lastCheckedDay = currentDay;
        morningActivation = false;
        noonActivation = false;
        eveningActivation = false;
        midnightActivation = true;
        for (CelestialEvent celestialEvent : MIDNIGHT_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.activateEvent(time, RANDOM);
        }
        stopMorningEvents();
        stopNoonEvents();
    }

    public static void stopMorningEvents() {
        for (CelestialEvent celestialEvent : MORNING_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.stopEvent();
        }
    }

    public static void stopNoonEvents() {
        for (CelestialEvent celestialEvent : NOON_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.stopEvent();
        }
    }

    public static void stopEveningEvents() {
        for (CelestialEvent celestialEvent : EVENING_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.stopEvent();
        }
    }

    public static void stopMidnightEvents() {
        for (CelestialEvent celestialEvent : MIDNIGHT_LIST) {
            if (celestialEvent == null) continue;
            celestialEvent.stopEvent();
        }
    }
}
