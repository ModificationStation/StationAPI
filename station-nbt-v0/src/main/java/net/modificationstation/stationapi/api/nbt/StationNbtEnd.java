package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtEnd;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtEnd extends StationNbtElement {

    @Override
    default NbtEnd copy() {
        return Util.assertImpl();
    }
}
