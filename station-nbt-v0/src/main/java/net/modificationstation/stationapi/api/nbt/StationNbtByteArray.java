package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtByteArray;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtByteArray extends StationNbtElement {
    @Override
    default NbtByteArray copy() {
        return Util.assertImpl();
    }
}
