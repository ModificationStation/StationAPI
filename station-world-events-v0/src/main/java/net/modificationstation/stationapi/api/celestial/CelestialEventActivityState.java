package net.modificationstation.stationapi.api.celestial;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class CelestialEventActivityState extends PersistentState {
    public boolean active;

    public CelestialEventActivityState(String id) {
        super(id);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.active = nbt.getBoolean("active");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("active", active);
    }
}
