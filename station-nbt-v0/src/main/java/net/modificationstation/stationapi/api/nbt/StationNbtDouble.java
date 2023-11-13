package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtDouble;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtDouble extends StationNbtElement {
    @Override
    default NbtDouble copy() {
        return Util.assertImpl();
    }
}
