package net.modificationstation.stationapi.api.celestial;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CelestialEvent {
    private final String name;
    private final int frequency;
    private float chance = 1;
    private int dayLength = 24000;
    private int dayOffset = 0;
    private int startingDaytime = 0;
    private int endingDaytime = 0;
    private int extraDays = 0;
    private boolean active;
    private final List<CelestialEvent> incompatibleEvents = new LinkedList<>();

    public CelestialEvent(int frequency, String name) {
        this.frequency = frequency;
        this.name = name;
    }

    public CelestialEvent setChance(float chance) {
        this.chance = chance;
        return this;
    }

    public CelestialEvent setDayLength(int dayLength) {
        this.dayLength = dayLength;
        return this;
    }

    public CelestialEvent setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
        return this;
    }

    public CelestialEvent setExtraDays(int extraDays) {
        this.extraDays = extraDays;
        return this;
    }

    public boolean activateEvent(long worldTime, Random random) {
        if (active) {
            return true;
        }
        for (CelestialEvent otherEvent : incompatibleEvents) {
            if (otherEvent == null) continue;
            if (otherEvent.isActive()) {
                active = false;
                return false;
            }
        }
        long days = worldTime / dayLength + dayOffset;
        active = days % frequency == 0 && random.nextFloat() <= chance;
        if (active) System.out.println(name + " has begun");
        return active;
    }

    public void updateEvent(long worldTime) {
        if (!active) return;
        worldTime -= startingDaytime;
        worldTime += endingDaytime;
        long days = worldTime / dayLength + dayOffset;
        active = days % frequency <= extraDays;
        if (!active) System.out.println(name + " is over");
    }

    public void stopEvent() {
        if (active) {
            System.out.println("Stopping event " + name);
            active = false;
        }
    }

    public void setInterval(int startingDaytime, int endingDaytime) {
        this.startingDaytime = startingDaytime;
        this.endingDaytime = endingDaytime;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Adds events to prevent them from happening simultaneously.
     * Automatically adds incompatibility for both directions.
     * @param otherEvent
     */
    public void addIncompatibleEvent(CelestialEvent otherEvent) {
        if (incompatibleEvents.contains(otherEvent)) return;
        incompatibleEvents.add(otherEvent);
        otherEvent.addIncompatibleEvent(this);
    }
}
