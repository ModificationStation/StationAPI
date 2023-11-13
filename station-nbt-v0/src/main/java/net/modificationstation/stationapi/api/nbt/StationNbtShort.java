package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtShort;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtShort extends StationNbtElement {
    @Override
    default NbtShort copy() {
        return Util.assertImpl();
    }
}
