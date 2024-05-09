package net.modificationstation.stationapi.api.celestial;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CelestialEvent {
    private final String name;
    private final int frequency;
    private final float chance;
    private final int dayLength;
    private final int dayOffset;
    private boolean active;
    private final List<CelestialEvent> incompatibleEvents = new LinkedList<>();

    public CelestialEvent(int frequency, float chance, int dayLength, int dayOffset, String name) {
        this.frequency = frequency;
        this.chance = chance;
        this.dayLength = dayLength;
        this.dayOffset = dayOffset;
        this.name = name;
    }

    public CelestialEvent(int frequency, float chance, int dayLength, String name) {
        this(frequency, chance, dayLength, 0, name);
    }

    public CelestialEvent(int frequency, float chance, String name) {
        this(frequency, chance, 24000, name);
    }

    public CelestialEvent(int frequency, String name) {
        this(frequency, 1, name);
    }

    public boolean activateEvent(long worldTime, Random random) {
        System.out.println("Attempt activation for event " + name);
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
        return active;
    }

    public void stopEvent() {
        System.out.println("Stopping event " + name);
        active = false;
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
