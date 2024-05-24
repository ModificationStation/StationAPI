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
    private boolean initializationNeeded = true;
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
        CelestialEventActivityState activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
        if (initializationNeeded) {
            activityState = initializeEvent(activityState);
        }
        if (active) {
            activityState.active = true;
            activityState.attemptedActivation = true;
            activityState.markDirty();
            return true;
        }
        for (CelestialEvent otherEvent : incompatibleEvents) {
            if (otherEvent == null) continue;
            if (otherEvent.isActive()) {
                active = false;
                activityState.active = false;
                activityState.attemptedActivation = true;
                activityState.markDirty();
                return false;
            }
        }
        long days = worldTime / dayLength + dayOffset;
        if (!activityState.attemptedActivation) {
            active = days % frequency == 0 && random.nextFloat() <= chance;
        }
        if (active) {
            activityState.active = true;
            activityState.attemptedActivation = true;
            activityState.markDirty();
            onActivation();
        }
        return active;
    }

    public void onActivation() {
    }

    public void onDeactivation() {
    }

    public void updateEvent(long worldTime) {
        CelestialEventActivityState activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
        if (initializationNeeded) {
            activityState = initializeEvent(activityState);
        }
        if (!active && !activityState.active) return;
        worldTime -= startingDaytime;
        worldTime += endingDaytime;
        long days = worldTime / dayLength + dayOffset;
        active = days % frequency <= extraDays;
        activityState.active = active;
        if (!active) {
            onDeactivation();
            activityState.attemptedActivation = false;
        }
        activityState.markDirty();
    }

    private CelestialEventActivityState initializeEvent(CelestialEventActivityState activityState) {
        initializationNeeded = false;
        activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
        if (activityState == null) {
            activityState = new CelestialEventActivityState(this.name);
            this.world.setState(this.name, activityState);
        } else {
            active = activityState.active;
        }
        return activityState;
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

    public void markForInitialization() {
        CelestialEventActivityState activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
        initializeEvent(activityState);
        initializationNeeded = false;
    }
}
