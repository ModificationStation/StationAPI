package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtList extends StationNbtElement {

    @Override
    default NbtList copy() {
        return Util.assertImpl();
    }
}
