package net.modificationstation.stationapi.api.celestial;

import lombok.Getter;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Contains the most important logic for automatically starting and stopping the event.
 * It is recommended to add the event to CelestialTimeManger for automatic management.
 * Inheritance is possible to add custom logic.
 */
public class CelestialEvent {
    @Getter
    private final String name;
    private final int frequency;
    private float chance = 1;
    private int dayLength = 24000;
    private int dayOffset = 0;
    private int startingDaytime = 0;
    private int endingDaytime = 0;
    private int extraDays = 0;
    @Getter
    private boolean active;
    private boolean initializationNeeded = true;
    private final List<CelestialEvent> incompatibleEvents = new LinkedList<>();
    private final List<CelestialEvent> dependencies = new LinkedList<>();
    public final World world;

    /**
     * Primary constructor for the event, containing the required parameters.
     *
     * @param frequency Specifies how many day apart the events should be. The bigger the number, the longer the distance.
     * @param name Event name, snake_casing is recommended.
     * @param world World object, obtained from the register event.
     */
    public CelestialEvent(int frequency, String name, World world) {
        this.frequency = frequency;
        this.name = name;
        this.world = world;
        CelestialInitializer.addEvent(this);
    }

    /**
     * Builder method for optional chance value.
     *
     * @param chance Value ranging from 0.0F to 1.0F, representing percentage chance.
     * @return The object itself.
     */
    public CelestialEvent setChance(float chance) {
        this.chance = chance;
        return this;
    }

    /**
     * Builder method for optional day length value.
     * Intended to be used in dimensions where day time is different.
     * This will however need a custom time manager to account for the different day length.
     *
     * @param dayLength Length of day specified in ticks.
     * @return The object itself.
     */
    public CelestialEvent setDayLength(int dayLength) {
        this.dayLength = dayLength;
        return this;
    }

    /**
     * Builder method for optional day offset.
     *
     * @param dayOffset Offsets the frequency by the specified number of days.
     * @return The object itself.
     */
    public CelestialEvent setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
        return this;
    }

    /**
     * Builder method for optional extra days.
     *
     * @param extraDays Extends the event duration by the specified number of days.
     * @return The object itself.
     */
    public CelestialEvent setExtraDays(int extraDays) {
        this.extraDays = extraDays;
        return this;
    }

    /**
     * Attempts to activate the event. Checks if the activation already was attempted within the valid time frame.
     * Takes activation chance and incompatible events into account.
     * Method gets called by CelestialTimeManager in case the event is added to it.
     * Can be called manually as well.
     *
     * @param worldTime Current time in the world measured in ticks.
     * @param random Locally used randomizer, seed is irrelevant.
     * @return True if the event met the criteria to be activated, otherwise false.
     */
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
                activityState.active = false;
                activityState.attemptedActivation = true;
                activityState.markDirty();
                return false;
            }
        }
        for (CelestialEvent otherEvent : dependencies) {
            if (otherEvent == null) continue;
            if (!otherEvent.isActive()) {
                activityState.active = false;
                activityState.attemptedActivation = true;
                activityState.markDirty();
                return false;
            }
        }
        long days = (worldTime / dayLength) + dayOffset;
        if (days % frequency > extraDays) {
            activityState.attemptedActivation = false;
            return false;
        }
        if (!activityState.attemptedActivation) {
            active = days % frequency <= extraDays && random.nextFloat() <= chance;
        }
        if (active) {
            activityState.active = true;
            activityState.attemptedActivation = true;
            activityState.markDirty();
            onActivation();
        }
        return active;
    }

    /**
     * Called precisely on event activation.
     * Can be customized by inheriting from this class.
     * Has access to the world object for more versatility.
     */
    public void onActivation() {
    }

    /**
     * Called precisely on event deactivation.
     * Can be customized by inheriting from this class.
     * Has access to the world object for more versatility.
     */
    public void onDeactivation() {
    }

    /**
     * Updates the activity status of an already active event.
     * Used by CelestialTimeManager 4 times per day.
     * Checks for validity of the time interval but not for chance and incompatibility.
     *
     * @param worldTime Current time in the world measured in ticks.
     */
    public void updateEvent(long worldTime) {
        CelestialEventActivityState activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
        if (initializationNeeded) {
            activityState = initializeEvent(activityState);
        }
        if (!active && !activityState.active) return;
        worldTime -= startingDaytime;
        worldTime += endingDaytime;
        long days = (worldTime / dayLength) + dayOffset;
        active = days % frequency <= extraDays;
        activityState.active = active;
        if (!active) {
            onDeactivation();
            activityState.attemptedActivation = false;
        }
        activityState.markDirty();
    }

    /**
     * Initializes the event by synchronizing it with NBT data.
     * Only used internally.
     *
     * @param activityState CelestialEventActivityState (custom PersistentState) used for accessing NBT data.
     * @return The initialized CelestialEventActivityState.
     */
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

    /**
     * Manual way of stopping event.
     * Intended for command systems.
     */
    public void stopEvent() {
        onDeactivation();
        if (active) {
            CelestialEventActivityState activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
            if (initializationNeeded) {
                activityState = initializeEvent(activityState);
            }
            activityState.active = false;
            activityState.markDirty();
            System.out.println("Stopping event " + name);
            active = false;
        }
    }

    /**
     * Called by CelestialTimeManager to specify the valid time frame of the event.
     * Time values can be specified as 4 values representing ticks in a day:
     * Morning: 0, Noon: 6000, Evening: 12000, Midnight: 18000
     *
     * @param startingDaytime Beginning daytime of the event.
     * @param endingDaytime Ending daytime of the event.
     */
    public void setInterval(int startingDaytime, int endingDaytime) {
        this.startingDaytime = startingDaytime;
        this.endingDaytime = endingDaytime;
    }

    /**
     * Adds events to prevent them from happening simultaneously.
     * Automatically adds incompatibility for both directions.
     *
     * @param otherEvent Event to be added to the incompatibility list.
     */
    public void addIncompatibleEvent(CelestialEvent otherEvent) {
        if (incompatibleEvents.contains(otherEvent)) return;
        incompatibleEvents.add(otherEvent);
        otherEvent.addIncompatibleEvent(this);
    }

    /**
     * Adds dependency to an event, meaning another event has to happen for this one to happen.
     * Dependency only gets added into one direction.
     *
     * @param dependency Event to be added to the dependencies list.
     */
    public void addDependency(CelestialEvent dependency) {
        if (dependencies.contains(dependency)) return;
        dependencies.add(dependency);
    }

    /**
     * Used by CelestialTimeManager to handle an edge-case where events would not be loaded when reloading the same world.
     */
    public void markForInitialization() {
        CelestialEventActivityState activityState = (CelestialEventActivityState) this.world.getOrCreateState(CelestialEventActivityState.class, this.name);
        initializeEvent(activityState);
        initializationNeeded = false;
    }
}
