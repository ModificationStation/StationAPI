package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtByte;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtByte extends StationNbtElement {

    @Override
    default NbtByte copy() {
        return Util.assertImpl();
    }
}
