package net.modificationstation.stationapi.api.celestial;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

/**
 * Manages important NBT data to ensure correct saving and loading of active celestial events.
 */
public class CelestialEventActivityState extends PersistentState {
    public boolean active;
    public boolean attemptedActivation;

    public CelestialEventActivityState(String id) {
        super(id);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.active = nbt.getBoolean("active");
        this.attemptedActivation = nbt.getBoolean("attemptedActivation");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("active", active);
        nbt.putBoolean("attemptedActivation", attemptedActivation);
    }
}
