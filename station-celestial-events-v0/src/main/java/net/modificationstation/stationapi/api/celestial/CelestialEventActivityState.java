package net.modificationstation.stationapi.api.celestial;

import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.PersistentState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.celestial.NbtCompoundAccessor;

import java.util.HashMap;

/**
 * Manages important NBT data to ensure correct saving and loading of active celestial events.
 */
public class CelestialEventActivityState extends PersistentState {

    public static final String ID = "stationapi_celestial_events";

    private HashMap<Identifier, Boolean> activeEvents = new HashMap<>();
    private HashMap<Identifier, Boolean> attemptedActivatedEvents = new HashMap<>();

    public CelestialEventActivityState(String id) {
        super(id);
    }

    public void set(Identifier identifier, boolean active, boolean attemptedActivate) {
        activeEvents.put(identifier, active);
        attemptedActivatedEvents.put(identifier, attemptedActivate);
        markDirty();
    }

    public void setActive(Identifier identifier, boolean active) {
        activeEvents.put(identifier, active);
        markDirty();
    }

    public void setAttemptedActivate(Identifier identifier, boolean attemptedActivate) {
        attemptedActivatedEvents.put(identifier, attemptedActivate);
        markDirty();
    }

    public boolean isActive(Identifier identifier) {
        return activeEvents.containsKey(identifier) && activeEvents.get(identifier);
    }

    public boolean hasAttemptedActivation(Identifier identifier) {
        return attemptedActivatedEvents.containsKey(identifier) && attemptedActivatedEvents.get(identifier);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        HashMap<Identifier, Boolean> activeEvents = new HashMap<>();
        //noinspection unchecked
        ((NbtCompoundAccessor) nbt.getCompound("activeEvents")).getEntries().forEach((ide, nbtBoolean) -> activeEvents.put(Identifier.of((String) ide), ((NbtByte) nbtBoolean).value == 1));
        this.activeEvents = activeEvents;

        HashMap<Identifier, Boolean> attemptedActivatedEvents = new HashMap<>();
        //noinspection unchecked
        ((NbtCompoundAccessor) nbt.getCompound("activeEvents")).getEntries().forEach((ide, nbtBoolean) -> activeEvents.put(Identifier.of((String) ide), ((NbtByte) nbtBoolean).value == 1));
        this.attemptedActivatedEvents = attemptedActivatedEvents;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        NbtCompound compound = new NbtCompound();
        activeEvents.forEach((identifier, aBoolean) -> compound.putBoolean(String.valueOf(identifier), aBoolean));
        nbt.put("activeEvents", compound);

        NbtCompound compound1 = new NbtCompound();
        attemptedActivatedEvents.forEach((identifier, aBoolean) -> compound1.putBoolean(String.valueOf(identifier), aBoolean));
        nbt.put("attemptedActivatedEvents", (NbtElement) compound);
    }
}
