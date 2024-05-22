package net.modificationstation.stationapi.api.celestial;

import net.minecraft.world.World;

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
    public final World world;

    public CelestialEvent(int frequency, String name, World world) {
        this.frequency = frequency;
        this.name = name;
        this.world = world;
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
        if (active) {
            onActivation();
        }
        return active;
    }

    public void onActivation() {
    }

    public void onDeactivation() {
    }

    public void updateEvent(long worldTime) {
        /*
        This piece of code for saving the activity state should probably be moved somewhere else. It is also incomplete and not fully functional
        CelestialEventActivityState activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
        if (activityState == null) {
            activityState = new CelestialEventActivityState(this.name);
            this.world.setState(this.name, activityState);
            System.out.println("Did not find activity state");
        } else {
            System.out.println("Found existing activity state");
        }
         */
        if (!active) return;
        worldTime -= startingDaytime;
        worldTime += endingDaytime;
        long days = worldTime / dayLength + dayOffset;
        active = days % frequency <= extraDays;
        if (!active) onDeactivation();
    }

    public void stopEvent() {
        onDeactivation();
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

    public String getName() {
        return this.name;
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
