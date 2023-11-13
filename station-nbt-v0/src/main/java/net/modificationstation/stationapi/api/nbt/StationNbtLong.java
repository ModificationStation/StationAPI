package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtLong;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtLong extends StationNbtElement {
    @Override
    default NbtLong copy() {
        return Util.assertImpl();
    }
}
