package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtFloat;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtFloat extends StationNbtElement {
    @Override
    default NbtFloat copy() {
        return Util.assertImpl();
    }
}
