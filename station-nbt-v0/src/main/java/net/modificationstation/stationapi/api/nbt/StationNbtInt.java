package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtInt;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtInt extends StationNbtElement {
    @Override
    default NbtInt copy() {
        return Util.assertImpl();
    }
}
