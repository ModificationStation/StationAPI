package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.ListTag;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtList extends StationNbtElement {

    @Override
    default ListTag copy() {
        return Util.assertImpl();
    }
}
